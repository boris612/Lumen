package hr.fer.zpr.lumen.coingame.model;

import java.util.List;

public class CoinField {

    public List<Coin> coins;



    public int sumOfCoins(){
        int sum=0;
        for(Coin c:coins){
            sum+=c.value;
        }
        return sum;
    }
}
