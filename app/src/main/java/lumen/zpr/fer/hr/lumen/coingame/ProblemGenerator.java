package lumen.zpr.fer.hr.lumen.coingame;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

/**
 * ovaj razred generira problem novcica.
 * U sebi takodjer sadrzi i optimalno rjesenje.
 * Created by ddarian on 12.12.17..
 */

public abstract class ProblemGenerator {
    /**
     * najveci mogući broj, zasad 10
     */
    public static final int MAX_NUMBER = 10;
    /**
     * velicina liste posljedne generiranih brojeva,
     * zasad 5
     */
    private static final int QUEUE_SIZE = 5;
    //potrebno je dodati apstrakciju rjesenja zadatka
    //kao clanske varijable koja se mijenja s obzirom na tekuci zadatak

    /**
     * generator slucajnih brojeva za stvaranje problema
     */
    private Random numberGenerator;

    /**
     * trenutna vrijednost novcica za koju se rjesava zadatak
     */
    private int currentNumber;

    private Queue<Integer> recentNumbers = new ArrayDeque<>(QUEUE_SIZE);

    /**
     * Metoda generira broj novcica koje ce biti potrebno sloziti.
     *
     * @return slijedeci zadatak broja novcica
     */
    public int generirajBroj() {
        int number;
        //generira se broj koji nije bio "u zadnje vrijeme"
        do {
            number = numberGenerator.nextInt(MAX_NUMBER + 1);
        } while (recentNumbers.contains(number));
        //dodaj broj u red generiranih
        recentNumbers.add(number);
        currentNumber = number;
        return number;
    }

    /**
     * Metoda vraca <code>true</code> ako je primljeno rjesenje optimalno.
     *
     * @return <tt>true</tt> ako je primljeno rjesenje optimalno
     */
    //zasad nema argumenta, nije dogovorena apstrakcija rjesenja
    public boolean isOptimal() {
        return false;
    }

    /**
     * @return {@linkplain #currentNumber}
     */
    public int getCurrentNumber() {
        return currentNumber;
    }
}
