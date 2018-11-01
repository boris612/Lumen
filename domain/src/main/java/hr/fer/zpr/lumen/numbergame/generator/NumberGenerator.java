package hr.fer.zpr.lumen.numbergame.generator;

import java.util.Random;

public class NumberGenerator {

    private Random rand = new Random();

    public int randomNumber(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public int randomNumber(int min, int max, int... without) {

        int number = rand.nextInt((max - min) + 1) + min;
        boolean flag;
        do {
            flag = false;
            for (int aWithout : without)
                if (number == aWithout) {
                    flag = true;
                    number = rand.nextInt((max - min) + 1) + min;
                    break;
                }
        } while (flag);
        return number;
    }

}