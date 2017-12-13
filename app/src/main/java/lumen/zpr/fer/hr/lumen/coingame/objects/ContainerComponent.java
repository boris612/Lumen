package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import lumen.zpr.fer.hr.lumen.CoinComponent;
import lumen.zpr.fer.hr.lumen.coingame.ConstantsUtil;
import lumen.zpr.fer.hr.lumen.coingame.GamePanel;
import lumen.zpr.fer.hr.lumen.coingame.ProblemGenerator;

/**
 * Spremnik u koji se dovlace komponente {@link CoinGameComponent}.
 * Created by Zlatko on 12-Dec-17.
 */

public class ContainerComponent implements CoinGameObject {
    /**
     * Graficka reprezentacija kontejnera
     */
    private Rect rect;
    /**
     * Pozicija na kojoj se nalazi labela
     */
    private Point labelPoint;
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

    /**
     * Konstruktor.
     *
     * @param gamePanel      panel kojem pripada novonastali kontejner
     * @param rect           pravokutnik, graficka reprezentacija kontejnera
     * @param labelPoint     mjesto na kojem se iscrtava labela
     * @param generator      generator zadatka
     * @param scoreComponent komponenta koja prikazuje trenutni broj bodova
     */
    public ContainerComponent(GamePanel gamePanel, Rect rect, Point labelPoint, ProblemGenerator generator, CoinComponent scoreComponent) {
        this.gamePanel = gamePanel;
        this.rect = rect;
        this.labelPoint = labelPoint;
        this.generator = generator;
        this.scoreComponent = scoreComponent;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        switch (state) {
            case INVALID_RESULT:
                paint.setColor(Color.rgb(204, 217, 255));
                break;
            case NOT_OPTIMAL_RESULT:
                paint.setColor(Color.YELLOW);
                break;
            case OPTIMAL_RESULT:
                paint.setColor(Color.GREEN);
        }
        canvas.drawRect(rect, paint);

        if (state == ContainerState.OPTIMAL_RESULT) {
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.BLACK);
        }
        paint.setTextSize(ConstantsUtil.CONTAINER_LABEL_FONT);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Iznos: " + Integer.toString(generator.getCurrentNumber()), labelPoint.x, labelPoint.y, paint);
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
            if (generator.isOptimal(getValues())) {
                state = ContainerState.OPTIMAL_RESULT;
            } else {
                state = ContainerState.NOT_OPTIMAL_RESULT;
            }
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

    /**
     * Reprezentacija trenutnog stanja igre.
     */
    public enum ContainerState {
        /**
         * Rezultat nije valjan
         */
        INVALID_RESULT,
        /**
         * Rezultat je valjan, ali nije optimalan
         */
        NOT_OPTIMAL_RESULT,
        /**
         * Rezultat je valjan i optimalan
         */
        OPTIMAL_RESULT
    }
}
