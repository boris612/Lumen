package hr.fer.zpr.lumen.ui.coingame.util;

import android.widget.TextView;

import hr.fer.zpr.lumen.numbergame.manager.NumberGameConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameMode;
import hr.fer.zpr.lumen.numbergame.manager.NumbergameManager;
import hr.fer.zpr.lumen.numbergame.manager.Operation;


public class NumberGamePresenter {

    private NumbergameManager numbergameManager;
    private TextView firstNumberTv;
    private TextView secondNumberTv;
    private TextView operationTv;

    public NumberGamePresenter(TextView firstNumberTv, TextView secondNumberTv, TextView operationTv, Operation operation ){
        this.firstNumberTv=firstNumberTv;
        this.secondNumberTv=secondNumberTv;
        this.operationTv=operationTv;
        numbergameManager=new NumbergameManager(operation);
    }

    public void newEquation(){
        numbergameManager.newEquation();
        firstNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getFirstNumber()));
        operationTv.setText(numbergameManager.getEquationGenerator().operationSign());
        secondNumberTv.setText(String.valueOf(numbergameManager.getEquationGenerator().getSecondNumber()));

    }

    public void checkSolution(int solution){
        if(isSolution(solution)){
            showSolutionIsCorrect();
        }
        else
            showSolutionIsIncorrect();
    }

    private void showSolutionIsIncorrect() {
        //todo
    }

    private void showSolutionIsCorrect() {
        //todo
    }

    private boolean isSolution(int solution){
        if(NumberGameConstants.numberGameMode==NumberGameMode.CHECKANSWER &&numbergameManager.getEquationGenerator().getAnswer()==solution) {
            return true;
        }
        if(NumberGameConstants.numberGameMode==NumberGameMode.CHECKFIRSTNUMBER&&numbergameManager.getEquationGenerator().getFirstNumber()==solution) {
            return true;
        }
        if(NumberGameConstants.numberGameMode==NumberGameMode.CHECKSECONDNUMBER&&numbergameManager.getEquationGenerator().getSecondNumber()==solution) {
            return true;
        }
        return false;

    }

}
