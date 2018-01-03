package lumen.zpr.fer.hr.lumen.coingame.objects;

/**
 * Reprezentacija trenutnog stanja igre
 */
public enum ContainerState {
    /**
     * Rezultat nije valjan
     */
    INVALID_RESULT,
    /**
     * Rezultat je valjan, ali nije optimalan
     */
    NOT_OPTIMAL_RESULT,
    /**
     * Rezultat je valjan i optimalan
     */
    OPTIMAL_RESULT
}
