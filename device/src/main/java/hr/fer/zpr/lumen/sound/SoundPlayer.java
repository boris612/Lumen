package hr.fer.zpr.lumen.sound;

public interface SoundPlayer<T> {

    void play(T file);

    boolean isPlaying();

    void stopPlaying();
}
