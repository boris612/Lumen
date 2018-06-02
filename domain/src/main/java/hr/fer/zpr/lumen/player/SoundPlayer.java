package hr.fer.zpr.lumen.player;

public interface SoundPlayer {

    void play(String file);

    boolean isPlaying();

    void stopPlaying();
}
