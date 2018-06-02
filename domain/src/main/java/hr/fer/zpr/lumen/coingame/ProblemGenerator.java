package hr.fer.zpr.lumen.coingame;

import java.util.List;

public interface ProblemGenerator {

    List<Integer> generateOptimalCoinsFor(int value);

    List<Integer> generateRandomCoins();

    int generateRandomNumber(int min, int max);

}
