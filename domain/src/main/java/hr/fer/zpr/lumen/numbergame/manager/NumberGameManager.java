package hr.fer.zpr.lumen.numbergame.manager;

import hr.fer.zpr.lumen.numbergame.generator.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class NumberGameManager {

    private Random rand=new Random();
    private Map<String,EquationGenerator> equationGenerators=new HashMap<>();
    private Operation operation=NumberGameConstants.operation;
    private Set<String> operations=NumberGameConstants.operations;
    private String activeEquationGenerator="0";
    public NumberGameManager(){
        int i=0;
        for (String s:operations) {
            switch(s){
                case "ADDITION":
                    equationGenerators.put(Integer.toString(i++),new AdditionEquationGenerator(EquationConstants.additionLimit));
                    break;
                case "SUBTRACTION":
                    equationGenerators.put(Integer.toString(i++),new AdditionEquationGenerator(EquationConstants.subtractionLimit));
                    break;
                case "MULTIPLICATION":
                    equationGenerators.put(Integer.toString(i++),new AdditionEquationGenerator(EquationConstants.multiplicationLimit));
                    break;
                case "DIVISION":
                    equationGenerators.put(Integer.toString(i++),new AdditionEquationGenerator(EquationConstants.divisionLimit));
                    break;
            }
        }


    }

    public EquationGenerator getEquationGenerator() {
        return equationGenerators.get(activeEquationGenerator);
    }

    public boolean checkAnswer(int answer){
        return answer==equationGenerators.get(activeEquationGenerator).getAnswer();
    }

    public boolean checkFirstNumber(int firstNumber){
        return firstNumber==equationGenerators.get(activeEquationGenerator).getFirstNumber();
    }

    public boolean checkSecondNumber(int secondNumber){
        return secondNumber==equationGenerators.get(activeEquationGenerator).getSecondNumber();
    }

    public void newEquation(){
        int numberOfGenerator=rand.nextInt(equationGenerators.size());
        activeEquationGenerator=Integer.toString(numberOfGenerator);
        equationGenerators.get(activeEquationGenerator).generateEquation();

    }
}
