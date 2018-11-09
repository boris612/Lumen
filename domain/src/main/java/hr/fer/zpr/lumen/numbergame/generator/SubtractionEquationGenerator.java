package hr.fer.zpr.lumen.numbergame.generator;

public class SubtractionEquationGenerator extends EquationGenerator {


    /**
     * @param limit Represents highest possible value of first number
     */
    private int lowerLimit=0;
    public SubtractionEquationGenerator(int limit) {
        this.limitNumber = limit;
        switch (limit){
            case 20:
                lowerLimit=1;
                break;
            case 100:
                lowerLimit=20;
                break;
            case 1000:
                lowerLimit=100;
                break;
            default:
                break;
        }
        generateEquation();
    }

    public void generateEquation() {
        NumberGenerator numberGenerator = new NumberGenerator();
        firstNumber = numberGenerator.randomNumber(lowerLimit, limitNumber);
        answer = numberGenerator.randomNumber(1, firstNumber);
        secondNumber = firstNumber - answer;
    }

    public String operationSign(){
        return "-";
    }

}
