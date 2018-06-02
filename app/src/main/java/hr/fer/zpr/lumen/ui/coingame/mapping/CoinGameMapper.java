package hr.fer.zpr.lumen.ui.coingame.mapping;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.ui.DebugUtil;
import hr.fer.zpr.lumen.ui.coingame.models.CoinFieldModel;
import hr.fer.zpr.lumen.ui.coingame.models.CoinModel;
import hr.fer.zpr.lumen.ui.coingame.models.NumberLabel;
import hr.fer.zpr.lumen.ui.coingame.util.CoinGameConstants;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;

public class CoinGameMapper {

    private SharedPreferences preferences;

    private Context context;

    private int screenWidth;

    private int screenHeight;

    public CoinGameMapper(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public List<CoinModel> getModelsForCoins(List<Coin> coins) {
        List<CoinModel> result = new ArrayList<>();
        List<Rect> rects = new ArrayList<>();
        List<Integer> positions = generatePositions(coins);

        int perRow = coins.size() % 2 == 0 ? coins.size() / 2 : coins.size() / 2 + 1;
        Random random = new Random();
        int coinDimension = (int) (screenWidth * CoinGameConstants.COIN_IMAGE_DIMENSION_FACTOR);
        int billWidth = coinDimension;
        int coinSpace = (int) (screenWidth * CoinGameConstants.COIN_SPACE_FACTOR);
        int billHeight = (int) (billWidth * CoinGameConstants.BILL_HEIGHT_FACTOR);
        for (Coin c : coins) {
            try {
                int position = positions.get(random.nextInt(positions.size()));
                positions.remove(new Integer(position));
                Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(c.image.path));
                Rect r = new Rect();
                if (c.value != 10) {
                    r.top = (int) (coinDimension * (position / perRow) + screenHeight * CoinGameConstants.FIRST_COIN_ROW_IMAGE_FACTOR);
                    r.bottom = r.top + coinDimension;
                    r.left = (int) (screenHeight * CoinGameConstants.LEFT_SPACE_FACTOR) + (position % perRow) * coinDimension + (position % perRow) * coinSpace;
                    r.right = r.left + coinDimension;
                } else {
                    r.top = (int) (coinDimension * (position / perRow) + screenHeight * CoinGameConstants.FIRST_COIN_ROW_IMAGE_FACTOR);
                    r.bottom = r.top + billHeight;
                    r.left = (int) (screenHeight * CoinGameConstants.LEFT_SPACE_FACTOR) + (position % perRow) * coinDimension + (position % perRow) * coinSpace;
                    r.right = r.left + billWidth;
                }
                result.add(new CoinModel(image, r, c.value));
            } catch (Exception e) {
                Log.d("error", e.getMessage());
            }
        }
        return result;
    }

    private List<Integer> generatePositions(List<Coin> coins) {
        List<Integer> positions = new ArrayList<>();
        int value = 0;
        for (Coin c : coins) {
            positions.add(value);
            value++;
        }
        return positions;
    }

    public CoinFieldModel createField() {
        Rect rect = new Rect();
        rect.left = (int) (screenWidth * CoinGameConstants.FIELD_LEFT_SIDE_FACTOR);
        rect.right = screenWidth;
        rect.top = 0;
        rect.bottom = (int) (screenHeight * CoinGameConstants.FIELD_HEIGHT_FACTOR);
        return new CoinFieldModel(rect);
    }

    public hr.fer.zpr.lumen.ui.viewmodels.CoinModel createHintCoin() throws Exception {
        Rect rect = new Rect();
        rect.top = 0;
        rect.left = 0;
        rect.bottom = (int) (screenHeight * ViewConstants.COIN_DIMENSION_FACTOR);
        rect.right = rect.bottom;
        return new hr.fer.zpr.lumen.ui.viewmodels.CoinModel(BitmapFactory.decodeStream(context.getAssets().open(hr.fer.zpr.lumen.ui.viewmodels.CoinModel.coinImagePath)), rect, preferences.getInt(ViewConstants.PREFERENCES_COINS, 0));
    }

    public NumberLabel goalLabel(int value) {
        Rect rect = new Rect();
        int width = (int) (screenWidth * CoinGameConstants.LABEL_WIDTH_FACTOR);
        rect.left = (int) (screenWidth * CoinGameConstants.GOAL_LABEL_LEFT_SIDE_FACTOR);
        rect.right = rect.left + width;
        rect.top = (int) (screenHeight * CoinGameConstants.LABEL_SPACE_FACTOR);
        rect.bottom = rect.top + width;
        return new NumberLabel(rect, value, Color.GREEN);
    }

    public NumberLabel currentLabel() {
        Rect rect = new Rect();
        int width = (int) (screenWidth * CoinGameConstants.LABEL_WIDTH_FACTOR);
        rect.left = (int) (screenWidth * CoinGameConstants.GOAL_LABEL_LEFT_SIDE_FACTOR);
        rect.right = rect.left + width;
        rect.top = (int) (screenHeight * CoinGameConstants.LABEL_SPACE_FACTOR * 2 + width);
        rect.bottom = rect.top + width;
        return new NumberLabel(rect, 0, Color.YELLOW);


    }

    public ImageModel generateTick() {
        try {
            Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(ViewConstants.TICK_IMAGE_PATH));
            Rect rect = new Rect();
            rect.left = screenWidth / 2 - screenWidth / 5;
            rect.top = screenHeight / 2 - screenHeight / 5;
            rect.right = screenWidth / 2 + screenWidth / 5;
            rect.bottom = screenHeight / 2 + screenHeight / 5;
            return new ImageModel(image, rect);
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }
        return null;
    }
}
