package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.content.SharedPreferences;
import android.widget.TextView;

import hr.fer.zpr.lumen.numbergame.manager.NumberGameConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameMode;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameManager;
import hr.fer.zpr.lumen.numbergame.manager.NumberGamePreferences;

import javax.inject.Inject;
import java.util.Random;


public class NumberGamePresenter {

    private NumberGameManager numbergameManager;
    private TextView firstNumberTv;
    private TextView secondNumberTv;
    private TextView operationTv;
    private TextView resultTv;
    private Random rand=new Random();



    public NumberGamePresenter(TextView firstNumberTv, TextView secondNumberTv, TextView resultTv, TextView operationTv ){

        this.firstNumberTv=firstNumberTv;
        this.secondNumberTv=secondNumberTv;
        this.operationTv=operationTv;
        this.resultTv=resultTv;
        numbergameManager=new NumberGameManager();
    }

    public TextView newEquation(){
        String gamemode= NumberGameConstants.numberGameMode;
        numbergameManager.newEquation();
        operationTv.setText(numbergameManager.getEquationGenerator().operationSign());
        if(gamemode.equals("CHECKANSWER")){
        firstNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getFirstNumber()));
        secondNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getSecondNumber()));
        return resultTv;
        }
        else{
            double permutationDecider=rand.nextDouble();
            if(permutationDecider<=0.45){
                secondNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getSecondNumber()));
                resultTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getAnswer()));
                return firstNumberTv;
            }
            else if(permutationDecider<=0.9){
                firstNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getFirstNumber()));
                resultTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getAnswer()));
                return secondNumberTv;
            }
            else{
                firstNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getFirstNumber()));
                secondNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getSecondNumber()));
                return resultTv;
            }
        }

    }

    public boolean checkSolution(){
        if(numbergameManager.getEquationGenerator().getAnswer()==Integer.parseInt(resultTv.getText().toString())
                &&numbergameManager.getEquationGenerator().getFirstNumber()==Integer.parseInt(firstNumberTv.getText().toString())
                &&numbergameManager.getEquationGenerator().getSecondNumber()==Integer.parseInt(secondNumberTv.getText().toString()))return true;
        return false;
    }






}
