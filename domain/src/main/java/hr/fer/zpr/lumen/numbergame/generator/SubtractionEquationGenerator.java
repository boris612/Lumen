package hr.fer.zpr.lumen.numbergame.generator;

public class SubtractionEquationGenerator extends EquationGenerator {


    /**
     * @param limit Represents highest possible value of first number
     */
    public SubtractionEquationGenerator(int limit) {
        this.limitNumber = limit;
        generateEquation();
    }

    public void generateEquation() {
        NumberGenerator numberGenerator = new NumberGenerator();
        firstNumber = numberGenerator.randomNumber(0, limitNumber);
        answer = numberGenerator.randomNumber(1, firstNumber);
        secondNumber = firstNumber - answer;
    }


}
