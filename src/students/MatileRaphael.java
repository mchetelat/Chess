package students;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.rulebased.RuleBasedAgent;
import students.matileraphael.rulebased.rules.*;

import java.util.ArrayList;
import java.util.List;

public class MatileRaphael implements Agent {

    private RuleBasedAgent agent;

    public MatileRaphael() {
        List<IRule> rules = new ArrayList<>();
        rules.add(new TouchDownRule()); // 1.0
        rules.add(new CaptureRule()); // 0.95
        rules.add(new IncreaseAttackedPawnsDefenseRule()); // 0.75
        rules.add(new CreateDefenseRule()); // 0.70
        rules.add(new StraightForwardRule()); // 0.5

        this.agent = new RuleBasedAgent(rules);
    }

    /* (non-Javadoc)
     * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#developerAlias()
     */
    @Override
    public String developerAlias() {
        // TODO If you want to use a pseudonym instead of your real name on the (published) list of results
        // return your pseudonym here
        return "rmatil";
    }

    /* (non-Javadoc)
     * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#nextMove(ch.uzh.ifi.ddis.pai.chessim.game.Color, ch.uzh.ifi.ddis.pai.chessim.game.SlidingWindow, ch.uzh.ifi.ddis.pai.chessim.game.History, long)
     */
    @Override
    public Move nextMove(Color yourColor, Board board, History history, long timeLimit) {
        // white color is represented by X on the display
        return this.agent.makeNextMove(new Context(yourColor, board, history));
    }

}
