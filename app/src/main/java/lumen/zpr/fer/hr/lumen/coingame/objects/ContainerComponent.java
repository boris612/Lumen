package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Zlatko on 12-Dec-17.
 */

public class ContainerComponent implements CoinGameObject {

    private Rect rect;
    private Point labelPoint;
    private Integer value = 0;

    public ContainerComponent(Rect rect, Point labelPoint) {
        this.rect = rect;
        this.labelPoint = labelPoint;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.RED);
        canvas.drawRect(rect, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText(value.toString(), labelPoint.x, labelPoint.y, paint);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean isSelected(int x, int y) {
        return false;
    }


}
