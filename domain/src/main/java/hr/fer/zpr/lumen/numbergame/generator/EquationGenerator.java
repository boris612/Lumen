package hr.fer.zpr.lumen.numbergame.generator;

public abstract class EquationGenerator implements IEquationGenerator {
    protected int firstNumber = 0;
    protected int secondNumber = 0;
    protected int answer = 0;

    /**
     * Used to determine limits of operation. Actual meaning depends on the operation.
     * For example for addition ir represents the maximum possible sum to be generated
     * while for multiplication it represents the maximum value of a factor
     * */
    protected int limitNumber = 0;

    abstract public void generateEquation();


    public int getAnswer() {
        return answer;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }
}
