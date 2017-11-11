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
 * Created by Korisnik on 11.11.2017..
 */

public class GameSound {
    public MediaPlayer wordRecording;
    public List<MediaPlayer> lettersRedording;

    public GameSound(Context context, String wordRecordingPath, Collection<String> lettersRecordingPath){
       // wordRecording = loadWordRecording(context,wordRecordingPath);
        lettersRedording = loadLettersRecording(context, lettersRecordingPath);
    }


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

}
