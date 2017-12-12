package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Spremnik u koji se dovlace komponente {@link CoinComponent}.
 * Created by Zlatko on 12-Dec-17.
 */

public class ContainerComponent implements CoinGameObject {
    /**
     * Graficka reprezentacija kontejnera
     */
    private Rect rect;
    /**
     * Mjesto na kojem se iscrtava labela s trenutnim stanjem kontejera
     */
    private Point labelPoint;
    /**
     * Vrijednost kovanica koje su trenutno u kontejneru
     */
    private Integer value = 0;
    /**
     * Lista kovanica koje su trenutno u kontejneru
     */
    private List<CoinComponent> coinsInside = new ArrayList<>();

    /**
     * Konstruktor.
     *
     * @param rect       pravokutnik, graficka reprezentacija kontejnera
     * @param labelPoint mjesto na kojem se iscrtava labela
     */
    public ContainerComponent(Rect rect, Point labelPoint) {
        this.rect = rect;
        this.labelPoint = labelPoint;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.rgb(204, 217, 255));
        canvas.drawRect(rect, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText(value.toString(), labelPoint.x, labelPoint.y, paint);
    }

    @Override
    public void update() {
        for (CoinComponent coin : new ArrayList<>(coinsInside)) {
            Point position = coin.getPosition();
            if (!isSelected(position.x, position.y)) {
                removeCoin(coin);
            }
        }
    }

    @Override
    public boolean isSelected(int x, int y) {
        return rect.contains(x, y);
    }

    public void addCoin(CoinComponent coin) {
        if (coinsInside.contains(coin)) {
            return;
        }
        coinsInside.add(coin);
        value += coin.getValue();
    }

    public void removeCoin(CoinComponent coin) {
        coinsInside.remove(coin);
        value -= coin.getValue();
    }
}
