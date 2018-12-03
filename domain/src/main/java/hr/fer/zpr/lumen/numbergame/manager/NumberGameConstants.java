package hr.fer.zpr.lumen.numbergame.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NumberGameConstants {
    public static String numberGameMode=NumberGameMode.CHECKANSWER.name();
    public static String inputMode=NumberGameMode.ONDRAG.name();
    public static Operation operation=Operation.ADDITION;
    public static Set<String> operations=new HashSet<>();
    {
        operations.add("ADDITION");
    }
    public static void setNumberGameMode(String numberGameMode) {
        NumberGameConstants.numberGameMode = numberGameMode;
    }

    public static void setInputMode(String inputMode) {
        NumberGameConstants.inputMode = inputMode;
    }

    public static void setOperation(Operation operation) {
        NumberGameConstants.operation = operation;
    }
}
