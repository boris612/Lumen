package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lumen.zpr.fer.hr.lumen.CoinComponent;
import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.coingame.ConstantsUtil;
import lumen.zpr.fer.hr.lumen.coingame.GamePanel;
import lumen.zpr.fer.hr.lumen.coingame.ProblemGenerator;

/**
 * Spremnik u koji se dovlace komponente {@link CoinGameComponent}.
 * Created by Zlatko on 12-Dec-17.
 */

public class ContainerComponent implements CoinGameObject {
    /**
     * Color specification of the {@linkplain ContainerState#INVALID_RESULT}
     */
    private static final int NEUTRAL_CONTAINER_COLOR = Color.rgb(203, 217, 255);
    private static Map<ContainerState, Integer> stateColorMap = new HashMap<>();
    /**
     * Graficka reprezentacija kontejnera
     */
    private Rect rect;
    /**
     * Pozicija na kojoj se nalazi labela ciljne vrijednosti
     */
    private Point targetLabelPoint;
    /**
     * Pozicija na kojoj se nalazi labela trenunte vrijednosti
     */
    private Point currentValueLabelPoint;
    /**
     * Vrijednost kovanica koje su trenutno u kontejneru
     */
    private Integer value = 0;
    /**
     * Lista kovanica koje su trenutno u kontejneru
     */
    private List<CoinGameComponent> coinsInside = new ArrayList<>();
    /**
     * Trenutno stanje kontejnera
     */
    private ContainerState state = ContainerState.INVALID_RESULT;
    /**
     * Generator trazenog broja, sluzi i za provjeravanje je li rjesenje tocno
     */
    private ProblemGenerator generator;
    /**
     * Komponenta koja prikazuje osvojeni broj bodova
     */
    private CoinComponent scoreComponent;
    /**
     * Sluzi za odbrojavanje do sljedeceg zadatka
     */
    private Long nextGameTime;
    /**
     * Gamepanel kojem ovaj kontejner pripada
     */
    private GamePanel gamePanel;

    private SharedPreferences preferences;

    private Context context;

    /**
     * Konstruktor.
     *
     * @param gamePanel              panel kojem pripada novonastali kontejner
     * @param rect                   pravokutnik, graficka reprezentacija kontejnera
     * @param targetLabelPoint       mjesto na kojem se iscrtava labela ciljne vrijednosti
     * @param currentValueLabelPoint mjesto na kojem se iscrtava labela
     * @param generator              generator zadatka
     * @param scoreComponent         komponenta koja prikazuje trenutni broj bodova
     */
    public ContainerComponent(GamePanel gamePanel, Rect rect, Point targetLabelPoint,
                              Point currentValueLabelPoint, ProblemGenerator generator,
                              CoinComponent scoreComponent,SharedPreferences pref,Context context) {
        preferences=pref;
        this.gamePanel = gamePanel;
        this.rect = rect;
        this.context=context;
        this.targetLabelPoint = targetLabelPoint;
        this.currentValueLabelPoint = currentValueLabelPoint;
        this.generator = generator;
        this.scoreComponent = scoreComponent;
        fillStateColorMap();
    }

    /**
     * Popunjava mapu bojama za određeni {@linkplain ContainerState}.
     */
    private void fillStateColorMap() {
        stateColorMap.put(ContainerState.OPTIMAL_RESULT, Color.GREEN);
        stateColorMap.put(ContainerState.NOT_OPTIMAL_RESULT, Color.YELLOW);
        stateColorMap.put(ContainerState.INVALID_RESULT, NEUTRAL_CONTAINER_COLOR);

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(stateColorMap.get(state));
        canvas.drawRect(rect, paint);

        paint.setTextSize(ConstantsUtil.CONTAINER_LABEL_FONT);
        paint.setTextAlign(Paint.Align.CENTER);

        switch (state) {
            case OPTIMAL_RESULT:
                canvas.drawText("Bravo!!!", targetLabelPoint.x, targetLabelPoint.y, paint);
                break;
            case NOT_OPTIMAL_RESULT:
                canvas.drawText("Točno je, ali može i bolje", currentValueLabelPoint.x,
                        currentValueLabelPoint.y, paint);
                canvas.drawText("Cilj: " + Integer.toString(generator.getCurrentNumber()),
                        targetLabelPoint.x, targetLabelPoint.y, paint);
                break;
            default:
                paint.setColor(Color.BLACK);
                canvas.drawText("Cilj: " + Integer.toString(generator.getCurrentNumber()),
                        targetLabelPoint.x, targetLabelPoint.y, paint);
                canvas.drawText("Trenutni iznos: " + Integer.toString(value),
                        currentValueLabelPoint.x, currentValueLabelPoint.y, paint);
        }
    }

    @Override
    public void update() {
        for (CoinGameComponent coin : new ArrayList<>(coinsInside)) {
            Point position = coin.getPosition();
            if (!isSelected(position.x, position.y)) {
                removeCoin(coin);
            }
        }

        if (state == ContainerState.OPTIMAL_RESULT) {

            if (nextGameTime == null) {
                nextGameTime = System.currentTimeMillis() + ConstantsUtil.MILLIS_WAITING;
                scoreComponent.addCoins(1);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt(context.getResources().getString(R.string.coins),scoreComponent.getCoins());
                editor.commit();
                return;
            }
            if (System.currentTimeMillis() >= nextGameTime) {
                nextGameTime = null;
                generator.generirajBroj();
                resetCoinPositions();
                updateState();
            }
        }
    }

    @Override
    public boolean isSelected(int x, int y) {
        return rect.contains(x, y);
    }

    /**
     * Osvjezava stanje igre ovisno o sadrzaju kontejnera.
     */
    private void updateState() {
        if (value == generator.getCurrentNumber()) {
            state = generator.isOptimal(getValues()) ?
                    ContainerState.OPTIMAL_RESULT : ContainerState.NOT_OPTIMAL_RESULT;
        } else {
            state = ContainerState.INVALID_RESULT;
        }
    }

    /**
     * Dodaje komponentu u interni spremnik, sluzi za dodavanje komponenti koje su unutar kontejnera.
     *
     * @param coin komponenta dovucena unutar kontejnera
     */
    public void addCoin(CoinGameComponent coin) {
        if (coinsInside.contains(coin)) {
            return;
        }
        coinsInside.add(coin);
        value += coin.getValue();
        updateState();
    }

    /**
     * Izbacuje komponentu iz internog spremnika, sluzi za izbacivanje komponenti koje su odvucene izvan kontejnera.
     *
     * @param coin komponenta izvucena izvan kontejnera
     */
    public void removeCoin(CoinGameComponent coin) {
        coinsInside.remove(coin);
        value -= coin.getValue();
        updateState();
    }

    /**
     * Vraca trenutnu vrijednost komponenti unutar kontejnera.
     *
     * @return trenutna vrijednost unutar kontejnera
     */
    public int getValue() {
        return value;
    }

    /**
     * Vraca listu vrijednosti komponenti koje se nalaze u kontejneru.
     *
     * @return lista vrijednosti
     */
    public List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        for (CoinGameComponent coin : coinsInside) {
            values.add(coin.getValue());
        }
        return values;
    }

    /**
     * Resetira pozicije svih komponenti unutar kontejnera na inicijalne pozicije.
     */
    private void resetCoinPositions() {
        for (CoinGameComponent coin : coinsInside) {
            coin.resetPosition();
        }
    }

    public ContainerState getState() {
        return state;
    }
}
