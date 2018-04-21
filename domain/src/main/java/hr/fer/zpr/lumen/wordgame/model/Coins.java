package hr.fer.zpr.lumen.wordgame.model;

public class Coins {

    private int coins;

    public Coins(int coins){
        this.coins=coins;
    }

    public int getCoins(){
        return coins;
    }

    public void AddCoins(int coins){
        this.coins+=coins;
    }

    public void subtractCoins(int coins){
        this.coins-=coins;
    }
}
