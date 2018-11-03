package hr.fer.zpr.lumen.numbergame.manager;

public class EquationConstants {
    static int additionLimit =20;
    static int subtractionLimit =100;
    static int multiplicationLimit =10;
    static int divisionLimit =10;

    public static void setAdditionLimit(int additionLimit) {
        EquationConstants.additionLimit = additionLimit;
    }

    public static void setDivisionLimit(int divisionLimit) {
        EquationConstants.divisionLimit = divisionLimit;
    }

    public static void setMultiplicationLimit(int multiplicationLimit) {
        EquationConstants.multiplicationLimit = multiplicationLimit;
    }

    public static void setSubtractionLimit(int subtractionLimit) {
        EquationConstants.subtractionLimit = subtractionLimit;
    }
}
