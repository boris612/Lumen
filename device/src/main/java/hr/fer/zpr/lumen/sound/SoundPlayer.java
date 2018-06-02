package hr.fer.zpr.lumen.sound;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundPlayer {

    private MediaPlayer player;

    public SoundPlayer() {
        player = new MediaPlayer();
    }

    public void play(AssetFileDescriptor afd) {
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }
        player = new MediaPlayer();
        player.setVolume(1f, 1f);
        try {
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            player.prepare();
            player.start();
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void stopPlaying() {
        player.reset();
    }


}
