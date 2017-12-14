package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Enkapsulira audiozapise slova od koje se riječ sastoji, kao i audiozapis izgovora riječi
 * @author Matija Čavlović
 * @version 0.1
 */

public class GameSound {
    private MediaPlayer wordRecording;
    private List<MediaPlayer> lettersRedording;

    public GameSound(Context context, String wordRecordingPath, Collection<String> lettersRecordingPath){
        wordRecording = loadWordRecording(context, wordRecordingPath);
        lettersRedording = loadLettersRecording(context, lettersRecordingPath);
    }

    /**
     * Učitava audiozapis izgovora riječi
     * @param context Kontekst
     * @param path Putanja do audiozapisa.
     * @return
     */
    private MediaPlayer loadWordRecording(Context context, String path){

        MediaPlayer mediaPlayer=new MediaPlayer();
        try{
            AssetFileDescriptor descriptor = context.getAssets().openFd(path);
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();

            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mediaPlayer.prepare();

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return mediaPlayer;
    }

    /**
     * Učitava audiozapise slova od kojih se riječ sastoji
     * @param context Kontekst
     * @param paths Kolekcija putanja audiozapisa svih slova.
     * @return
     */
    private List<MediaPlayer> loadLettersRecording (Context context, Collection<String> paths){
        List<MediaPlayer> mpList = new ArrayList<>();

        for (String path:paths) {
            try {
                AssetFileDescriptor descriptor = context.getAssets().openFd(path);
                long start = descriptor.getStartOffset();
                long end = descriptor.getLength();
                MediaPlayer mediaPlayer=new MediaPlayer();
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
                mediaPlayer.prepare();
                mpList.add(mediaPlayer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mpList;
    }

    /**
     * Reproducira pojedinu riječ i slovkanje riječi
     * @param letterPauseLength Duljina pauze između izgovora pojedinog slova u milisekundama
     */
    public void playSpelling(int letterPauseLength){
        wordRecording.start();
        while (wordRecording.isPlaying()) ;

        try {
            Thread.sleep(letterPauseLength);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (MediaPlayer mp : this.lettersRedording) {
            mp.start();
            while (mp.isPlaying()) ;
            try {
                Thread.sleep(letterPauseLength);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Reproducira slovkanje pojedine riječi. Duljina pauze između izgovora pojedinog slova je jedna sekunda
     */
    public void playSpelling(){
        this.playSpelling(1000);
    }

}
