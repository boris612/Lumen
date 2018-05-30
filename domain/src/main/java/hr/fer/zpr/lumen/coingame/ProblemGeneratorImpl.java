package hr.fer.zpr.lumen.coingame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.coingame.model.CoinValues;
import hr.fer.zpr.lumen.coingame.repository.CoinGameRepository;

public class ProblemGeneratorImpl implements ProblemGenerator {



    @Override
    public List<Integer> generateCoinsFor(int value) {
        List<Integer> result=new ArrayList<>();
        int sum=0;
        Random random=new Random();
        while(sum<value){
            int randomCoin= CoinValues.coinValues.get(random.nextInt(CoinValues.coinValues.size()));
            if(sum+randomCoin<=value) sum+=randomCoin;
        }
        return result;
    }

    @Override
    public List<Integer> generateRandomCoins(int number) {
        List<Integer> result=new ArrayList<>();
        Random random=new Random();
        for(int i=0;i<number;i++){
            result.add(CoinValues.coinValues.get(random.nextInt(CoinValues.coinValues.size())));
        }
        return result;
    }
}
