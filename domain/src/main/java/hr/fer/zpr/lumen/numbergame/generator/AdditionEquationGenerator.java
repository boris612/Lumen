package hr.fer.zpr.lumen.numbergame.generator;

public class AdditionEquationGenerator extends EquationGenerator {


    /**
     * @param limit Represents highest possible answer
     * */
    private int lowerLimit=1;
    NumberGenerator numberGenerator = new NumberGenerator();
    public AdditionEquationGenerator(int limit) {
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

        answer = numberGenerator.randomNumber(lowerLimit, limitNumber);
        firstNumber = numberGenerator.randomNumber(0, answer);
        if((firstNumber==0||firstNumber==answer)&&Math.random()<=0.5){
            firstNumber=numberGenerator.randomNumber(answer/2,answer-1);
        }
        secondNumber = answer - firstNumber;
    }

    public String operationSign(){
        return "+";
    }


}
