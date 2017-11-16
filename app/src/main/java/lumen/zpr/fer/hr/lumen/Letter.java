package lumen.zpr.fer.hr.lumen;

import lumen.zpr.fer.hr.lumen.LetterImage;

/**
 * Created by Alen on 14.11.2017..
 */

public class Letter {
    private Character letter;
    private LetterImage letterImage;

    public Letter(Character letter, LetterImage image) {
        this.letter = letter;
        this.letterImage = image;
    }

    public Character getLetter() {
        return letter;
    }

    public LetterImage getLetterImage() {
        return letterImage;
    }
}
