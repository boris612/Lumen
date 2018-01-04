package lumen.zpr.fer.hr.lumen.coingame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lumen.zpr.fer.hr.lumen.CoinComponent;
import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.coingame.objects.CoinGameComponent;
import lumen.zpr.fer.hr.lumen.coingame.objects.CoinGameObject;
import lumen.zpr.fer.hr.lumen.coingame.objects.ContainerComponent;

/**
 * Razred koji sadrzi trenutno stanje igre i brine se o njenom toku. Zna iscrtavati trenutno stanje igre.
 * Created by Zlatko on 12-Dec-17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    /**
     * Komparator za {@link CoinGameObject}, sortira tako je oznacen novcic na kraju liste
     */
    private static Comparator<CoinGameObject> coinGameObjectComparator = new Comparator<CoinGameObject>() {
        @Override
        public int compare(CoinGameObject c1, CoinGameObject c2) {
            boolean s1 = c1.getSelection();
            boolean s2 = c2.getSelection();

            if (s1 && !s2) {
                return 1;
            }
            if (!s1 && s2) {
                return -1;
            }
            return 0;
        }
    };
    /**
     * Dretva koja osvjezava stanje ove igre
     */
    private MainThread thread;
    /**
     * Lista novcica koji se koriste
     */
    private List<CoinGameComponent> coins = new ArrayList<>();
    /**
     * Container u koji se dovlace selektirani novcici
     */
    private ContainerComponent container;
    /**
     * Generira trazene iznose i provjerava tocna rjesenja
     */
    private ProblemGenerator generator = new ProblemGenerator();
    /**
     * Komponenta koja prikazuje trenutni broj bodova
     */
    private CoinComponent scoreView;
    /**
     * Kontekst
     */
    private Context context;

    /**
     *Za zeljeno ponasanje prilikom pritiska na back ili lockscreena
     */
    public boolean paused = false;
    public boolean terminated=false;

    private SharedPreferences preferences;
    /**
     * Konstruktor.
     *
     * @param context kontekst
     */
    public GamePanel(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        preferences=context.getSharedPreferences(getResources().getString(R.string.preference_file),Context.MODE_PRIVATE);


        generator.generirajBroj();
        //Todo maknuti u metodu
        generator.setCoins(Arrays.asList(1, 1, 1, 2, 2, 2, 5, 5, 10));
        generateCoins(Arrays.asList(1, 1, 1, 2, 2, 2, 5, 5, 10));
        int coins=preferences.getInt(getResources().getString(R.string.coins),0);
        scoreView = new CoinComponent(getResources().getDrawable(R.drawable.smaller_coin), coins, getContext());

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        container = new ContainerComponent(this,
                new Rect((int) ((1 - ConstantsUtil.CONTAINER_WIDTH) * widthPixels),
                        (int) (ConstantsUtil.PADDING * heightPixels),
                        (int) ((1 - ConstantsUtil.PADDING) * widthPixels),
                        (int) (ConstantsUtil.CONTAINER_HEIGHT * heightPixels)),
                new Point((int) (ConstantsUtil.CONTAINER_LABEL_X * widthPixels),
                        (int) (ConstantsUtil.CONTAINER_LABEL_Y * heightPixels)),
                new Point((int) (ConstantsUtil.CURRENT_VALUE_LABEL_X * widthPixels),
                        (int) (ConstantsUtil.CURRENT_VALUE_LABEL_Y * heightPixels)),
                generator, scoreView, preferences, context);
    }

    /**
     * Metoda koja nasumicno mijesa vrijednosti u polju.
     *
     * @param ar polje
     */
    private static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = 0; i < ar.length; i++) {
            int index = rnd.nextInt(ar.length);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread!=null && thread.isInterrupted()){
            thread.setRunning(true);
            return;
        }
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        while(true) {
            try {
                thread.setRunning(false);
                thread.join();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (CoinGameComponent coin : coins) {
                    if (coin.isSelected((int) event.getX(), (int) event.getY())) {
                        coin.setSelection(true);
                        break;
                    }
//                    else {
//                        coin.setSelection(false);
//                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                CoinGameComponent component = null;
                for (CoinGameComponent coin : coins) {
//                    if (coin.isSelected((int) event.getX(), (int) event.getY())) {
//                        coin.update(new Point((int) event.getX(), (int) event.getY()));
//                        break;
//                    }
                    if (coin.getSelection()) {
                        component = coin;
                        break;
                    }
                }

                if (component != null) {
                    component.update(new Point((int) event.getX(), (int) event.getY()));
                }
                break;
            case MotionEvent.ACTION_UP:
                for (CoinGameComponent coin : coins) {
                    Point position = coin.getPosition();
                    if (container.isSelected(position.x, position.y)) {
                        container.addCoin(coin);
                    }
                    coin.setSelection(false);
                }
        }
        return true;
    }

    /**
     * Osigurava da elementi igre budu "up to date".
     */
    public void update() {
        container.update();
        Collections.sort(coins, coinGameObjectComparator);
    }

    @Override
    public void draw(Canvas canvas) {
        /*
        Za testiranje,  performanse slabe
        u drugoj igri prilikom klika na back igra nije responsive na par sek, baca exceptione
        pogledat i testirat u debugeru, bez doljnjeg ifa
         */
        if(canvas == null) return;


        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        scoreView.draw(canvas);

        container.draw(canvas);



        for (CoinGameComponent coin : coins) {
            coin.draw(canvas);
        }
    }

    /**
     * Generira novcice zadanih vrijednosti i postavlja ih na nasumicne pozicije na dnu ekrana.
     *
     * @param values vrijednosti novcica koji se generiraju
     */
    public void generateCoins(List<Integer> values) {
        coins.clear();

        int maxPositions = (int) ((1 / ConstantsUtil.POSITION_SIZE));
        int[] positionsArray = new int[2 * maxPositions];
        for (int i = 0; i < 2 * maxPositions; i++) {
            positionsArray[i] = i;
        }
        shuffleArray(positionsArray);

        for (int i = 0; i < values.size(); i++) {
            int posX = (int) ((positionsArray[i] % maxPositions + 1) * ConstantsUtil.POSITION_SIZE * context.getResources().getDisplayMetrics().widthPixels);
            int posY = positionsArray[i] < maxPositions ? (int) (ConstantsUtil.POSITION_INIT_Y * context.getResources().getDisplayMetrics().heightPixels) : (int) ((1 - ConstantsUtil.POSITION_SIZE) * context.getResources().getDisplayMetrics().heightPixels);
            coins.add(new CoinGameComponent(getDrawable(values.get(i)), new Point(posX, posY), values.get(i), (int) (context.getResources().getDisplayMetrics().widthPixels * ConstantsUtil.COIN_SIZE)));
        }
    }

    /**
     * Vraca sliku novcica sa zadanom vrijednoscu.
     *
     * @param value vrijednost novcica
     * @return drawable objekt
     */
    private Drawable getDrawable(int value) {
        switch (value) {
            case 1:
                return getResources().getDrawable(R.drawable.kuna_1);
            case 2:
                return getResources().getDrawable(R.drawable.kuna_2);
            case 5:
                return getResources().getDrawable(R.drawable.kuna_5);
            case 10:
                return getResources().getDrawable(R.drawable.kuna_10);
            default:
                throw new IllegalArgumentException("Coin with given value does not exist: " + value);
        }
    }
}
