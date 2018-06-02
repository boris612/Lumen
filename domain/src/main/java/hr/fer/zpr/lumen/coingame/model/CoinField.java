package hr.fer.zpr.lumen.coingame.model;

import java.util.ArrayList;
import java.util.List;

public class CoinField {

    public List<Integer> coins = new ArrayList<>();


    public int sumOfCoins() {
        int sum = 0;
        for (Integer c : coins) {
            sum += c;
        }
        return sum;
    }

    public void clearField() {
        coins.clear();
    }

    public List<Integer> coinToIntList() {
        List<Integer> result = new ArrayList<>();
        for (Integer c : coins) {
            result.add(c);
        }
        return result;
    }

    public void insertCoinIntoField(int c) {
        coins.add(c);
    }

    public void removeCoinFromField(int c) {
        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i) == c) {
                coins.remove(i);
                break;
            }
        }
    }
}
