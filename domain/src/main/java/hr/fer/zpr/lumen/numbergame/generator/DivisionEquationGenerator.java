package hr.fer.zpr.lumen.numbergame.generator;

public class DivisionEquationGenerator extends EquationGenerator {

    /**
     * @param limit Represents highest possible value of the result and second number
     * */
    private int lowerLimit=0;
    public DivisionEquationGenerator(int limit) {
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
        NumberGenerator numberGenerator = new NumberGenerator();
        answer = numberGenerator.randomNumber(lowerLimit, limitNumber);
        secondNumber = numberGenerator.randomNumber(1, limitNumber);
        firstNumber=answer*secondNumber;
    }

    public String operationSign(){
        return "\u00F7";
    }
}
