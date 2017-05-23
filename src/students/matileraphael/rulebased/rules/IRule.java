package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

/**
 * Specifies a particular rule which only depends on the current state of the game in
 * order to propose a particular move.
 */
public interface IRule {

    /**
     * The maximum incentive a move may have.
     */
    int MAX_INCENTIVE = 1;

    /**
     * Proposes a new rule based on the specified context.
     * <p>
     * Note, that the evaluation score in the returned {@link Pair} <i>MUST</i> be
     * normalized to a value between 0 and {@link IRule#MAX_INCENTIVE}!
     *
     * @param context The context of the game
     *
     * @return A proposed move, or null, if none was found
     */
    Pair<Float, Move> getMove(Context context);
}
