package hr.fer.zpr.lumen.numbergame.manager;

public class NumberGameConstants {
    public static NumberGameMode numberGameMode=NumberGameMode.CHECKANSWER;
    public static Operation operation=Operation.ADDITION;
    public static void setNumberGameMode(NumberGameMode numberGameMode) {
        NumberGameConstants.numberGameMode = numberGameMode;
    }

    public static void setOperation(Operation operation) {
        NumberGameConstants.operation = operation;
    }
}
