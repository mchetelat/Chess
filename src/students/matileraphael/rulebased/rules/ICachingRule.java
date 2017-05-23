package students.matileraphael.rulebased.rules;

/**
 * An interface for a particular {@link IRule} which
 * demands, that its state has to be re-evaluated after each round.
 */
public interface ICachingRule extends IRule {

    /**
     * Clears the currently built cache and makes the rule ready
     * for a new, independent evaluation of the new game state.
     */
    void clearCache();
}
