package hr.fer.zpr.lumen.numbergame.generator;

public class AdditionEquationGenerator extends EquationGenerator {


    /**
     * @param limit Represents highest possible answer
     * */
    public AdditionEquationGenerator(int limit) {
        this.limitNumber = limit;
        generateEquation();
    }

    public void generateEquation() {
        NumberGenerator numberGenerator = new NumberGenerator();
        answer = numberGenerator.randomNumber(1, limitNumber);
        firstNumber = numberGenerator.randomNumber(0, answer);
        secondNumber = answer - firstNumber;
    }


}
