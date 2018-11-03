package hr.fer.zpr.lumen.numbergame.manager;

public class NumberGameConstants {
    public static NumberGameMode numberGameMode=NumberGameMode.CHECKANSWER;

    public static void setNumberGameMode(NumberGameMode numberGameMode) {
        NumberGameConstants.numberGameMode = numberGameMode;
    }
}
