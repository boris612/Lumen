package hr.fer.zpr.lumen.numbergame.generator;

import hr.fer.zpr.lumen.numbergame.manager.NumberGameConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameMode;
import hr.fer.zpr.lumen.numbergame.manager.NumberGamePreferences;

public class MultiplicationEquationGenerator extends EquationGenerator {


    /**
     * @param limit Represents highest possible value of a factor
     */
    NumberGenerator numberGenerator = new NumberGenerator();
    private int lowerLimit=0;
    public MultiplicationEquationGenerator(int limit) {
        limitNumber = limit;
        switch (limit){
            case 10:
                lowerLimit=0;
                break;
            case 20:
                lowerLimit=10;
                break;
            case 100:
                lowerLimit=20;
                break;
            default:
                break;
        }
        generateEquation();
    }

    public void generateEquation() {
        if(NumberGameConstants.numberGameMode.equals(NumberGameMode.PERMUTATIONS.name())){
            firstNumber=numberGenerator.randomNumber((lowerLimit==0)?lowerLimit+2:lowerLimit, limitNumber);
            secondNumber = numberGenerator.randomNumber(1, limitNumber);
        }
        else{
        firstNumber = numberGenerator.randomNumber(lowerLimit, limitNumber);
        secondNumber = numberGenerator.randomNumber(0, limitNumber);
        }
        answer = firstNumber * secondNumber;
    }

    public String operationSign(){
        return "x";
    }
}
