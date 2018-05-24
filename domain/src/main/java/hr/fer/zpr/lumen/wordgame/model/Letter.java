package hr.fer.zpr.lumen.wordgame.model;

public class Letter {

    public final String value;

    public Image image;

    public Sound sound;

    public Letter(String value){
        this.value=value;

    }

    public Letter(String value,Image image,Sound sound){
        this.value=value;
        this.image=image;
        this.sound=sound;
    }

}
