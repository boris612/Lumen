package hr.fer.zpr.lumen.ui.wordgame.mapping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zpr.lumen.ui.DebugUtil;
import hr.fer.zpr.lumen.ui.viewmodels.CoinModel;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterFieldModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.ui.wordgame.models.StartingHintModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewUtil;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;

public class WordGameMapper {

    private int screenWidth;

    private int screenHeight;

    private Context context;

    public WordGameMapper(Context context) {
        this.context = context;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }


    public ImageModel getTick() {
        try {
            Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(ViewConstants.TICK_IMAGE_PATH));
            Rect rect = new Rect();
            rect.left = screenWidth / 2 - screenWidth / 5;
            rect.top = screenHeight / 2 - screenHeight / 5;
            rect.right = screenWidth / 2 + screenWidth / 5;
            rect.bottom = screenHeight / 2 + screenHeight / 5;
            return new ImageModel(image, rect);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return null;
    }

    public ImageModel getImage(Word word) {
        try {
            Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(word.wordImage.path));
            Rect bounds = new Rect();
            bounds.left = screenWidth / 2 - (int) (screenWidth * ViewConstants.IMAGE_WIDTH_FACTOR / 2);
            bounds.right = screenWidth / 2 + (int) (screenWidth * ViewConstants.IMAGE_WIDTH_FACTOR / 2);
            bounds.top = (int) (screenHeight * ViewConstants.IMAGE_TOP_SPACE_FACTOR);
            bounds.bottom = (int) (screenHeight * ViewConstants.IMAGE_HEIGHT_FACTOR);
            return new ImageModel(image, bounds);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return null;
    }

    public StartingHintModel hintModel(Word word) {
        Rect rect = new Rect();
        rect.top = (int) (screenHeight * ViewConstants.STARTING_HINT_TOP_FACTOR);
        rect.bottom = rect.top + (int) (screenHeight * ViewConstants.STARTING_HINT_HEIGHT_FACTOR);
        rect.left = 0;
        rect.right = screenWidth;
        return new StartingHintModel(word, rect, screenWidth, screenHeight, context);
    }

    public List<LetterModel> mapLetters(Word word, List<Letter> randomLetters) {
        if (randomLetters == null) randomLetters = new ArrayList<>();
        int letterDimension = (int) (screenWidth / (word.letters.size() * (1 + ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR)) * ViewConstants.CHAR_FIELDS_WIDTH_FACTOR);
        if (letterDimension > screenWidth * ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR)
            letterDimension = (int) (screenWidth * ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR);
        int startingSpace = screenWidth / 100;
        int fieldDimension = letterDimension;
        letterDimension = (int) (letterDimension * ViewConstants.LETTER_IMAGE_SCALE_FACTOR);
        int space = (int) (screenWidth - (randomLetters.size() + word.letters.size()) * letterDimension - startingSpace) / (randomLetters.size() + word.letters.size());
        List<Rect> rects = new ArrayList<>();
        for (int i = 0, n = randomLetters.size() + word.letters.size(); i < n; i++) {
            Rect rect = new Rect();
            rect.left = startingSpace + i * (space + letterDimension);
            rect.right = rect.left + letterDimension;
            rect.top = (int) (screenHeight * ViewConstants.CHAR_FIELDS_Y_COORDINATE_FACTOR + fieldDimension + screenHeight * ViewConstants.FIELD_LETTER_SPACE_FACTOR);
            rect.bottom = rect.top + letterDimension;
            rects.add(rect);
        }
        List<LetterModel> letters = new ArrayList<>();
        randomLetters.addAll(word.letters);
        Random random = new Random();
        for (Letter letter : randomLetters) {
            try {
                Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(letter.image.path));
                int randIndex = random.nextInt(rects.size());
                letters.add(new LetterModel(letter.value, image, rects.get(randIndex)));
                rects.remove(randIndex);
            } catch (Exception e) {
                Log.d("error", e.getMessage());
            }
        }
        return letters;
    }

    public List<LetterModel> mapAllLetters(List<Letter> allLetters) {
        if (allLetters == null) allLetters = new ArrayList<>();

        List<LetterModel> letters = new ArrayList<>();
        for (Letter letter : allLetters) {
            try {
                Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(letter.image.path));
                letters.add(new LetterModel(letter.value, image, new Rect(20,50,230,200)));
            } catch (Exception e) {
                Log.d("error", e.getMessage());
            }
        }
        return letters;
    }

    public List<LetterFieldModel> createFields(Word word) {
        List<LetterFieldModel> fields = new ArrayList<>();
        int fieldDimension = (int) (screenWidth / (word.letters.size() * (1 + ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR)) * ViewConstants.CHAR_FIELDS_WIDTH_FACTOR);
        if (fieldDimension > screenWidth * ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR)
            fieldDimension = (int) (screenWidth * ViewConstants.CHAR_FIELD_WIDTH_MAX_FACTOR);
        int gapWidth = (int) (fieldDimension * ViewConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR);
        int startingX = ViewUtil.calculateStartingX(screenWidth, word.letters.size(), fieldDimension, gapWidth);
        for (int i = 0; i < word.letters.size(); i++) {
            Rect rect = new Rect();
            rect.left = startingX + i * (fieldDimension + gapWidth);
            rect.top = (int) (screenHeight * ViewConstants.CHAR_FIELDS_Y_COORDINATE_FACTOR);
            rect.right = rect.left + fieldDimension;
            rect.bottom = rect.top + fieldDimension;
            fields.add(new LetterFieldModel(rect));
        }
        return fields;
    }
    public CoinModel getCoinModel(int coin) {
        try {
            Rect rect = new Rect();
            rect.top = 0;
            rect.left = 0;
            rect.bottom = (int) (screenHeight * ViewConstants.COIN_DIMENSION_FACTOR);
            rect.right = rect.bottom;
            return new CoinModel(BitmapFactory.decodeStream(context.getAssets().open(CoinModel.coinImagePath)), rect, coin);
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }
        return null;
    }
}

