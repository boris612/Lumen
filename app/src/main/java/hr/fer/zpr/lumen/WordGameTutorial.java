package hr.fer.zpr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Alen on 10.1.2018..
 */

public class WordGameTutorial extends GameTutorial {
    private Drawable handImage;
    private CharacterField field;
    private LetterImage letter;
    private Point letterOriginalPos;
    private Point lastSetPosition;
    private static final int ANIMATION_SPEED = 4; //pixels per second
    private int framesToRestart;

    @Inject
    Context context;

    public WordGameTutorial(List<LetterImage> letters, CharacterField field, long timeToActivate) {
        super(timeToActivate);
        this.letter = determineLetterToInsert(letters, field);
        this.field = field;
        letterOriginalPos = letter.getCenter();
        framesToRestart = 0;
        this.handImage = context.getResources().getDrawable(R.drawable.hand);
        initHandPosition(context.getResources().getDisplayMetrics());
    }

    private void initHandPosition(DisplayMetrics dm) {
        int width = (int) (dm.widthPixels * GameLayoutConstants.HAND_IMAGE_WIDTH_FACTOR);
        int height = (int) (dm.widthPixels * GameLayoutConstants.HAND_IMAGE_HEIGHT_FACTOR);
        handImage.setBounds(0, 0, width, height);
        updateHandPosition();
    }

    private void updateHandPosition() {
        Rect imageBounds = handImage.getBounds();

        int forfX = (int) (imageBounds.width() * GameLayoutConstants.HAND_IMAGE_FOREFINGER_X_COORDINATE_FACTOR);
        int forfY = (int) (imageBounds.height() * GameLayoutConstants.HAND_IMAGE_FOREFINGER_Y_COORDINATE_FACTOR);

        int x = letter.getCenter().x - forfX;
        int y = letter.getCenter().y - forfY;

        handImage.setBounds(x, y, x + imageBounds.width(), y + imageBounds.height());
    }


    private LetterImage determineLetterToInsert(List<LetterImage> letters, CharacterField field) {
        for (LetterImage letter : letters) {
            if (field.getCorrectCharacter().equals(letter.getLetter())) {
                return letter;
            }
        }
        return null;
    }

    @Override
    public void updateTutorialAnimation() {
        if (framesToRestart > 0) {
            framesToRestart--;
            if (framesToRestart == 0) {
                letter.setCenter(letterOriginalPos);
            }
            return;
        }

        Point currentPos = letter.getCenter();
        Point aimPos = field.getCenterPoint();

        double angle = Math.atan2(aimPos.y - currentPos.y, aimPos.x - currentPos.x);

        int newX = (int) (currentPos.x + Math.cos(angle) * ANIMATION_SPEED);
        int newY = (int) (currentPos.y + Math.sin(angle) * ANIMATION_SPEED);
        letter.setCenter(new Point(newX, newY));

        updateHandPosition();

        double remainingPathLength = Math.sqrt(Math.pow(aimPos.x - newX, 2) + Math.pow(aimPos.y - newY, 2));
        if (field.collision(letter) && remainingPathLength < ANIMATION_SPEED) {
            letter.setCenter(field.getCenterPoint());
            framesToRestart = 50;
        }
        lastSetPosition = new Point(letter.getCenter().x, letter.getCenter().y);
    }

    @Override
    public void update() {
        if (!letter.getCenter().equals(lastSetPosition)) {
            letterOriginalPos = letter.getCenter();
        }
        super.update();
    }

    @Override
    public void restartIfNotShutDown() {
        letter.setCenter(letterOriginalPos);
        framesToRestart = 0;
        super.restartIfNotShutDown();
    }

    @Override
    public void drawTutorial(Canvas canvas) {
        handImage.draw(canvas);
    }
}
