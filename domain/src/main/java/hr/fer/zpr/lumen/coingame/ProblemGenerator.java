package hr.fer.zpr.lumen.coingame;

import java.util.List;

public interface ProblemGenerator {

    List<Integer> generateCoinsFor(int value);

    List<Integer> generateRandomCoins(int number);
}
