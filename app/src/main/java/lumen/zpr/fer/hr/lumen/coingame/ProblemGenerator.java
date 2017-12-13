package lumen.zpr.fer.hr.lumen.coingame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * ovaj razred generira problem novcica.
 * U sebi takodjer sadrzi i optimalno rjesenje.
 * Created by ddarian on 12.12.17..
 */

public class ProblemGenerator {
    /**
     * najmanji moguci broj
     */
    private static final int MIN_NUMBER = 2;
    /**
     * najveci mogući broj, zasad 10
     */
    private static final int MAX_NUMBER = 15;
    /**
     * velicina liste posljedne generiranih brojeva,
     * zasad 5
     */
    private static final int QUEUE_SIZE = 5;

    /**
     * generator slucajnih brojeva za stvaranje problema
     */
    private Random numberGenerator = new Random();
    /**
     * trenutna vrijednost novcica za koju se rjesava zadatak
     */
    private int currentNumber;
    /**
     * Lista vrijednosti novcica koja predstavlja optimalno rjesenje za {@linkplain #currentNumber}
     */
    private List<Integer> optimalSolution;
    /**
     * {@linkplain Queue} zadnje generiranih brojeva
     */
    private Queue<Integer> recentNumbers = new ArrayDeque<>(QUEUE_SIZE);

    private List<Integer> coins;

    /**
     * Metoda generira broj novcica koje ce biti potrebno sloziti.
     *
     * @return slijedeci zadatak broja novcica
     */
    public int generirajBroj() {
        int number;
        if (recentNumbers.size() == QUEUE_SIZE) {
            recentNumbers.remove();
        }
        //generira se broj koji nije bio "u zadnje vrijeme"
        do {
            number = numberGenerator.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
        } while (recentNumbers.contains(number));
        //dodaj broj u red generiranih
        recentNumbers.add(number);
        currentNumber = number;
        if (coins != null) {
            generateSolution();
        }
        return number;
    }

    public void setCoins(List<Integer> coins) {
        this.coins = coins;
        generateSolution();
    }

    public List<Integer> getCoins() {
        return coins;
    }

    private void generateSolution() {
        Collections.sort(coins, Collections.<Integer>reverseOrder());
        List<Integer> solution = new LinkedList<>();
        int sum = 0;

        for (int coin : coins) {
            if (sum + coin <= currentNumber) {
                solution.add(coin);
                if (sum < currentNumber) {
                    sum += coin;
                }
            }
        }

        optimalSolution = new ArrayList<>(solution);
    }

    /**
     * Metoda vraca <code>true</code> ako je primljeno rjesenje optimalno.
     *
     * @return <tt>true</tt> ako vrijedi <tt>optimalSolution.equals(solution)</tt>
     */
    public boolean isOptimal(List<Integer> solution) {
        Collections.sort(solution, Collections.<Integer>reverseOrder());
        return optimalSolution.equals(solution);
    }

    /**
     * @return {@linkplain #currentNumber}
     */
    public int getCurrentNumber() {
        return currentNumber;
    }

    public Queue<Integer> getRecentNumbers() {
        return recentNumbers;
    }

    public List<Integer> getOptimalSolution() {
        return optimalSolution;
    }

    /**
     * Resetira parametre razreda.
     */
    public void reset() {
        numberGenerator = new Random();
        currentNumber = 0;
        optimalSolution = null;
        recentNumbers = new ArrayDeque<>(QUEUE_SIZE);
    }
}
