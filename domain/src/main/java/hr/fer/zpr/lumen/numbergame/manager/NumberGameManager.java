package hr.fer.zpr.lumen.numbergame.manager;

import hr.fer.zpr.lumen.numbergame.generator.*;

public class NumberGameManager {


    private EquationGenerator equationGenerator;
    private Operation operation=NumberGameConstants.operation;
    public NumberGameManager(){
        switch(operation){
            case ADDITION:
                equationGenerator=new AdditionEquationGenerator(EquationConstants.additionLimit);
                break;
            case SUBTRACTION:
                equationGenerator=new SubtractionEquationGenerator(EquationConstants.subtractionLimit);
                break;
            case MULTIPLICATION:
                equationGenerator=new MultiplicationEquationGenerator(EquationConstants.multiplicationLimit);
                break;
            case DIVISION:
                equationGenerator=new DivisionEquationGenerator(EquationConstants.divisionLimit);
                break;
        }


    }

    public EquationGenerator getEquationGenerator() {
        return equationGenerator;
    }

    public boolean checkAnswer(int answer){
        return answer==equationGenerator.getAnswer();
    }

    public boolean checkFirstNumber(int firstNumber){
        return firstNumber==equationGenerator.getFirstNumber();
    }

    public boolean checkSecondNumber(int secondNumber){
        return secondNumber==equationGenerator.getSecondNumber();
    }

    public void newEquation(){
        equationGenerator.generateEquation();
    }
}
