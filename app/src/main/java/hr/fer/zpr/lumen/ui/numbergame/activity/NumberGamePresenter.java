package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.widget.TextView;

import hr.fer.zpr.lumen.numbergame.manager.NumberGameConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameMode;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameManager;


public class NumberGamePresenter {

    private NumberGameManager numbergameManager;
    private TextView firstNumberTv;
    private TextView secondNumberTv;
    private TextView operationTv;



    public NumberGamePresenter(TextView firstNumberTv, TextView secondNumberTv, TextView operationTv ){
        this.firstNumberTv=firstNumberTv;
        this.secondNumberTv=secondNumberTv;
        this.operationTv=operationTv;
        numbergameManager=new NumberGameManager();
    }

    public void newEquation(){
        numbergameManager.newEquation();
        firstNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getFirstNumber()));
        operationTv.setText(numbergameManager.getEquationGenerator().operationSign());
        secondNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getSecondNumber()));

    }

    public boolean checkSolution(int solution){
        if(isSolution(solution)){
            showSolutionIsCorrect();
            return true;
        }
        else
            showSolutionIsIncorrect();
        return false;
    }

    private void showSolutionIsIncorrect() {
        //todo
    }

    private void showSolutionIsCorrect() {
        //todo
    }

    private boolean isSolution(int solution){
        if(NumberGameConstants.numberGameMode==NumberGameMode.CHECKANSWER.name() &&numbergameManager.getEquationGenerator().getAnswer()==solution) {
            return true;
        }
        if(NumberGameConstants.numberGameMode==NumberGameMode.CHECKFIRSTNUMBER.name()&&numbergameManager.getEquationGenerator().getFirstNumber()==solution) {
            return true;
        }
        if(NumberGameConstants.numberGameMode==NumberGameMode.CHECKSECONDNUMBER.name()&&numbergameManager.getEquationGenerator().getSecondNumber()==solution) {
            return true;
        }
        return false;

    }


}
