package hr.fer.zpr.lumen.coingame.util;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zpr.lumen.coingame.model.Coin;

public class CoinUtil {

    public static boolean compareIntegerLists(List<Integer> first, List<Integer> second) {
        List<Integer> firstCopy = new ArrayList<>(first);
        List<Integer> secondCopy = new ArrayList<>(second);
        for (int i = 0,size=firstCopy.size(); i < size; i++) {
            int x = firstCopy.get(0);
            if (secondCopy.contains(x)) {
                firstCopy.remove(new Integer(x));
                secondCopy.remove(new Integer(x));
            } else {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> coinsToIntList(List<Coin> coins) {
        List<Integer> result = new ArrayList<>();
        for (Coin c : coins) {
            result.add(c.value);
        }
        return result;
    }
}
