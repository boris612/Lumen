package lumen.zpr.fer.hr.lumen.coingame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Dretva zaduzena za periodicno osvjezavanje trenutnog stanja igre.
 * Created by Zlatko on 12-Dec-17.
 */

public class MainThread extends Thread {
    /**
     * Maksimalni broj osvjezavanja u sekundi
     */
    private static final int MAX_FPS = 30;
    public static Canvas canvas;
    /**
     * Surface na koji se iscrtava igra
     */
    private SurfaceHolder surfaceHolder;
    /**
     * GamePanel koji sadrži trenutno stanje igre
     */
    private GamePanel gamePanel;
    /**
     * Zastavica stanja igre
     */
    private boolean running;

    /**
     * Konstruktor.
     *
     * @param surfaceHolder surface na koji se iscrtava igra
     * @param gamePanel     game panel koji sadrzi trenutno stanje igre
     */
    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMilis;
        long waitTime;
        long targetTime = 1000 / MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    synchronized (surfaceHolder) {
                        gamePanel.update();
                        gamePanel.draw(canvas);
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            timeMilis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMilis;
            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pokrece/zaustavlja igru.
     *
     * @param running igra se zaustavlja postavljanjem na false
     */
    void setRunning(boolean running) {
        this.running = running;
    }
}
