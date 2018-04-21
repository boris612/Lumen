package hr.fer.zpr.lumen.coingame.objects;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zpr.lumen.GameSoundListener;
import hr.fer.zpr.lumen.coingame.GamePanel;

/**
 * Created by Korisnik on 16.1.2018..
 */

public class MessageSound {
    private String correct1;
    private String correct2;
    private String correct12;
    private Thread thread;
    private boolean playing;
    private List<MediaPlayer> correctSounds;
    private List<MediaPlayer> tryAgainSounds;
    private List<GameSoundListener> listeners;
    private Context context;
    private GamePanel gamePanel;


    public MessageSound(Thread thread, Context context, GamePanel gamePanel) {
        correctSounds = new ArrayList<>();
        tryAgainSounds = new ArrayList<>();
        listeners = new ArrayList<>();
        this.thread = thread;
        this.context = context;
        this.gamePanel = gamePanel;
        addMessage("zvucniZapisi/poruke/točno.mp3", correctSounds);
        addMessage("zvucniZapisi/poruke/bravo.mp3", correctSounds);
        addMessage("zvucniZapisi/poruke/tocno, ali moze bolje.mp3", tryAgainSounds);
    }

    public void playCorrect() {
        MediaPlayer mp = correctSounds.get((int) System.currentTimeMillis() % 2);
        play(mp);
    }

    public void playTryAgain() {
        MediaPlayer mp = tryAgainSounds.get((int) System.currentTimeMillis() % 1);
        play(mp);
    }

    public void play(MediaPlayer mp) {

        if (gamePanel.terminated) return;
        while (gamePanel.paused) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mp.start();
        while (mp.isPlaying())
            ;

        boolean wasPaused = false;
        while (gamePanel.paused) {
            try {
                wasPaused = true;
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (wasPaused)
            try {
                wasPaused = true;
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        for (GameSoundListener l : listeners) {
            l.wholeSpellingDone();
        }
        playing = false;

    }

    public void registerListener(GameSoundListener listener) {
        listeners.add(listener);
    }

    public void setPlaying(boolean play) {
        this.playing = play;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void addMessage(String path, List<MediaPlayer> list) {

        AssetFileDescriptor descriptor = null;
        try {
            descriptor = context.getAssets().openFd(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long start = descriptor.getStartOffset();
        long end = descriptor.getLength();
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.add(mediaPlayer);
    }
}
