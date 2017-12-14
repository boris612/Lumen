package lumen.zpr.fer.hr.lumen.coingame;

import android.content.Context;
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
import java.util.List;
import java.util.Random;

import lumen.zpr.fer.hr.lumen.CoinComponent;
import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.coingame.objects.CoinGameComponent;
import lumen.zpr.fer.hr.lumen.coingame.objects.ContainerComponent;

/**
 * Razred koji sadrzi trenutno stanje igre i brine se o njenom toku. Zna iscrtavati trenutno stanje igre.
 * Created by Zlatko on 12-Dec-17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
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
     * Konstruktor.
     *
     * @param context kontekst
     */
    public GamePanel(Context context) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);

        generator.generirajBroj();
        //Todo maknuti u metodu
        generator.setCoins(Arrays.asList(new Integer[]{1, 1, 1, 2, 2, 2, 5, 5, 10}));
        generateCoins(Arrays.asList(new Integer[]{1, 1, 1, 2, 2, 2, 5, 5, 10}));

        scoreView = new CoinComponent(getResources().getDrawable(R.drawable.smaller_coin), 0, context);

        thread = new MainThread(getHolder(), this);

        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        container = new ContainerComponent(this, new Rect((int) ((1 - ConstantsUtil.CONTAINER_WIDTH) * widthPixels), (int) (ConstantsUtil.PADDING * heightPixels), (int) ((1 - ConstantsUtil.PADDING) * widthPixels), (int) (ConstantsUtil.CONTAINER_HEIGHT * heightPixels)), new Point((int) (ConstantsUtil.CONTAINER_LABEL_X * widthPixels), (int) (ConstantsUtil.CONTAINER_LABEL_Y * heightPixels)), generator, scoreView);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        while (true) {
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
            case MotionEvent.ACTION_MOVE:
                for (CoinGameComponent coin : coins) {
                    if (coin.isSelected((int) event.getX(), (int) event.getY())) {
                        coin.update(new Point((int) event.getX(), (int) event.getY()));
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                for (CoinGameComponent coin : coins) {
                    Point position = coin.getPosition();
                    if (container.isSelected(position.x, position.y)) {
                        container.addCoin(coin);
                    }
                }
        }
        return true;
    }

    /**
     * Osigurava da elementi igre budu "up to date".
     */
    public void update() {
        container.update();
    }

    @Override
    public void draw(Canvas canvas) {
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
                return getResources().getDrawable(R.drawable.coin_value_1);
            case 2:
                return getResources().getDrawable(R.drawable.coin_value_2);
            case 5:
                return getResources().getDrawable(R.drawable.coin_value_5);
            case 10:
                return getResources().getDrawable(R.drawable.coin_value_10);
            default:
                throw new IllegalArgumentException("Coin with given value does not exist: " + value);
        }
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
}