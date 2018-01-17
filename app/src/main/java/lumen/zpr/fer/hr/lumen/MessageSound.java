package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    Context context;

    public MessageSound(Thread thread,Context context) {
        correctSounds = new ArrayList<>();
        listeners = new ArrayList<>();
        this.thread=thread;
        this.context=context;
        addCorrectMessage("zvucniZapisi/poruke/točno.mp3");
        addCorrectMessage("zvucniZapisi/poruke/bravo.mp3");

    }

    public void playCorrect(){
        MediaPlayer mp = correctSounds.get((int)System.currentTimeMillis()%2);
        mp.start();
        while(mp.isPlaying())
            ;

        for(GameSoundListener l: listeners) {
            l.wholeSpellingDone();
        }

    }

    public void registerListener(GameSoundListener listener) {
        listeners.add(listener);
    }

    public void setPlaying (boolean play){
        this.playing=play;
    }
    public boolean isPlaying (){
        return this.playing;
    }

    private void addCorrectMessage(String path){
        AssetFileDescriptor descriptor = null;
        try {
            descriptor = context.getAssets().openFd(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long start = descriptor.getStartOffset();
        long end = descriptor.getLength();
        MediaPlayer mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        correctSounds.add(mediaPlayer);
    }
}
