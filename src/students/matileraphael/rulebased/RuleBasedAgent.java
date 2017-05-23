package students.matileraphael.rulebased;

import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;
import students.matileraphael.rulebased.rules.FallbackMoveRule;
import students.matileraphael.rulebased.rules.ICachingRule;
import students.matileraphael.rulebased.rules.IRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * An agent trying to play pawn chess according to some particular rules.
 */
public class RuleBasedAgent {

    Logger logger = Logger.getLogger(RuleBasedAgent.class.getName());

    private List<IRule> rules = new ArrayList<>();

    /**
     * Creates a new rule based agent, using the given rules to evaluate game moves
     *
     * @param rules The rules to evaluate the current state of the game
     */
    public RuleBasedAgent(List<IRule> rules) {
        if (null == rules) {
            throw new IllegalArgumentException("Some rules must be configured!");
        }

        this.rules = rules;
    }

    /**
     * Determine a next move based on the given game context.
     *
     * @param context The context of the game
     *
     * @return A move which is applicable to the current game state, null otherwise
     */
    public Move makeNextMove(Context context) {

        List<Pair<Float, Move>> bestMoves = new ArrayList<>();

        for (IRule rule : this.rules) {
            Pair<Float, Move> nextMovePair = rule.getMove(context);

            if (null != nextMovePair) {
                if (! context.getBoard().figureAt(nextMovePair.getValue().from).possibleMoves(context.getBoard()).containsKey(nextMovePair.getValue())) {
                    logger.warning("Found an invalid move: " + nextMovePair.getValue());
                }

                bestMoves.add(nextMovePair);
            }
        }

        // clear created cache for current moving state
        for (IRule rule : this.rules) {
            if (rule instanceof ICachingRule) {
                ((ICachingRule) rule).clearCache();
            }
        }

        Collections.sort(bestMoves);

        if (bestMoves.size() > 0) {
            return bestMoves.get(bestMoves.size() - 1).getValue();
        }

        logger.fine("Using fallback move: straight ahead...");
        return new FallbackMoveRule().getMove(context).getValue();
    }
}
