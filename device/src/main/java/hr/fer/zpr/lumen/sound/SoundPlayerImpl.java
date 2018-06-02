package hr.fer.zpr.lumen.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import hr.fer.zpr.lumen.player.SoundPlayer;

public class SoundPlayerImpl implements SoundPlayer {

    private MediaPlayer player;
    private AssetManager assets;

    public SoundPlayerImpl(AssetManager assets) {
        this.assets=assets;
        player = new MediaPlayer();
    }


    public void play(String file) {
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }
        player = new MediaPlayer();
        player.setVolume(1f, 1f);
        try {
            AssetFileDescriptor afd=assets.openFd(file);
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
