package hr.fer.zpr.lumen.coingame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zpr.lumen.coingame.model.CoinValues;

public class ProblemGeneratorImpl implements ProblemGenerator {

    public static final int MAX_RAND_COINS = 8;

    public static final int MIN_RAND_COINS = 4;

    private int lastValueGenerated = -1;


    @Override
    public List<Integer> generateOptimalCoinsFor(int value) {
        List<Integer> result = new ArrayList<>();
        int sum = 0;
        Integer[] sorted = CoinValues.coinValues.toArray(new Integer[0]);
        int index = sorted.length - 1;
        while (sum < value) {
            if (sum + sorted[index] <= value) {
                sum += sorted[index];
                result.add(sorted[index]);
            } else {
                index--;
            }
        }
        return result;
    }

    @Override
    public List<Integer> generateRandomCoins() {
        List<Integer> result = new ArrayList<>();
        Random random = new Random();
        int number = random.nextInt(MAX_RAND_COINS - MIN_RAND_COINS) + MIN_RAND_COINS;
        for (int i = 0; i < number; i++) {
            result.add(CoinValues.coinValues.get(random.nextInt(CoinValues.coinValues.size())));
        }
        return result;
    }

    @Override
    public int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        int number = rand.nextInt(max - min) + min;
        while (number == lastValueGenerated) number = rand.nextInt(max - min) + min;
        lastValueGenerated = number;
        return number;
    }
}
