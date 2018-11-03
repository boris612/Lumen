package hr.fer.zpr.lumen.numbergame.generator;

public class DivisionEquationGenerator extends EquationGenerator {

    /**
     * @param limit Represents highest possible value of the result and second number
     * */

    public DivisionEquationGenerator(int limit) {
        limitNumber = limit;
        generateEquation();
    }

    public void generateEquation() {
        NumberGenerator numberGenerator = new NumberGenerator();
        answer = numberGenerator.randomNumber(0, limitNumber);
        secondNumber = numberGenerator.randomNumber(1, limitNumber);
        firstNumber=answer*secondNumber;
    }
}
