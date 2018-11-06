package hr.fer.zpr.lumen.numbergame.generator;

public class MultiplicationEquationGenerator extends EquationGenerator {


    /**
     * @param limit Represents highest possible value of a factor
     */

    public MultiplicationEquationGenerator(int limit) {
        limitNumber = limit;
        generateEquation();
    }

    public void generateEquation() {
        NumberGenerator numberGenerator = new NumberGenerator();
        firstNumber = numberGenerator.randomNumber(0, limitNumber);
        secondNumber = numberGenerator.randomNumber(0, limitNumber);
        answer = firstNumber * secondNumber;
    }

    public String operationSign(){
        return "*";
    }
}
