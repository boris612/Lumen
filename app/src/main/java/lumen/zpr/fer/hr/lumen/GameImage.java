package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Enkapsulacija slike koja se prikazuje na GamePanelu. Sama se brine o svojoj poziciji
 * na ekranu (centrira se) kao i o svojoj veličini - podatke o rasporedu dohvaća iz GameLayoutConstants.
 */

public class GameImage {
    private Bitmap bitmap;
    private Rect rect;
    private String imageName;
    private double maxWidthFactor = GameLayoutConstants.IMAGE_MAX_WIDTH_FACTOR; //za 0.7 -> slika može u širinu ici najviše do 70% veličine displaya
    private  double maxHeightFactor = GameLayoutConstants.IMAGE_MAX_HEIGHT_FACTOR;
    private static double Y_COORDINATE_FACTOR = GameLayoutConstants.IMAGE_Y_COORDINATE_FACTOR; //za 0.1 -> y koordinata slike je na 10% visine ekrana (s obzirom da je landscape, pod visinom se misli na širinu)


    public GameImage(String imageName, Context context, boolean usePresentingPhaseDimensions) throws IOException {
        this.imageName = imageName;
        byte[] imageBytes = loadImageToByteArray(imageName,context);

        bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

        if(usePresentingPhaseDimensions) {
            maxWidthFactor = GameLayoutConstants.IMAGE_PRESENTING_MAX_WIDTH_FACTOR;
            maxHeightFactor = GameLayoutConstants.IMAGE_PRESENTING_MAX_HEIGHT_FACTOR;
        } else {
            maxWidthFactor = GameLayoutConstants.IMAGE_MAX_WIDTH_FACTOR;
            maxHeightFactor = GameLayoutConstants.IMAGE_MAX_HEIGHT_FACTOR;
        }

        rect = determineOptimalRect(context);
    }

    public String getImageName() {
        return imageName;
    }

    public Bitmap getBitmap() {
        return  bitmap;
    }

    public  Rect getRect() {
        return rect;
    }

    private Rect determineOptimalRect(Context context) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int display_width = dm.widthPixels;
        int display_height = dm.heightPixels;
        double scaleFactorW = 1;
        double scaleFactorH = 1;

        if(width > display_width*maxWidthFactor) {
            scaleFactorW = display_width*maxWidthFactor/width;
        }
        if(height > display_height*maxHeightFactor) {
            scaleFactorH = display_height*maxHeightFactor/height;
        }

        if(scaleFactorH < scaleFactorW) {
            width*=scaleFactorH;
            height*=scaleFactorH;
        } else {
            width*=scaleFactorW;
            height*=scaleFactorH;
        }

        int left = display_width/2-width/2;
        int top = (int)(display_height*Y_COORDINATE_FACTOR);
        return new Rect(left,top,left+width,top+height);
    }

    private byte[] loadImageToByteArray(String imageName, Context context) throws  IOException {
        List<Byte> byteList = new ArrayList<>();

        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(imageName);
            while (true) {
                int byt = is.read();
                if (byt == -1) {
                    break;
                }
                byteList.add((byte) byt);
            }
        } finally {
            if(is!=null) is.close();
        }

        byte[] byteArray = new byte[byteList.size()];
        for(int i = 0; i < byteArray.length; i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }

}
