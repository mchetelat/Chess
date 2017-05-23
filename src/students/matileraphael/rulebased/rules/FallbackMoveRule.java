package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.Map;

/**
 * This rule is used to make an arbitrary move by eating an enemy pawn or walking straight forward.
 */
public class FallbackMoveRule implements IRule {

    @Override
    public Pair<Float, Move> getMove(Context context) {
        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        for (Map.Entry<Coordinates, Figure> entry : context.getBoard().figures(context.getColor()).entrySet()) {
            // check coordinates on left and right above current position
            Coordinates upperLeft = new Coordinates(entry.getKey().getRow() + (directionFactor), entry.getKey().getColumn() - 1);
            Figure upperLeftFigure = context.getBoard().figureAt(upperLeft);

            Coordinates upperRight = new Coordinates(entry.getKey().getRow() + (directionFactor), entry.getKey().getColumn() + 1);
            Figure upperRightFigure = context.getBoard().figureAt(upperRight);

            Coordinates upperStraight = new Coordinates(entry.getKey().getRow() + (directionFactor), entry.getKey().getColumn());
            Figure upperStraightFigure = context.getBoard().figureAt(upperStraight);

            if (context.getBoard().onBoard(upperLeft)
                    && null != upperLeftFigure
                    && upperLeftFigure.color.equals(context.getColor().getOtherColor())) {
                // we are targeted by an enemy player, check whether we can beat him
                // if we take his position
                return new Pair<>(IRule.MAX_INCENTIVE / 1000f, new Move(entry.getKey(), upperLeft));
            }

            if (context.getBoard().onBoard(upperRight)
                    && null != upperRightFigure
                    && upperRightFigure.color.equals(context.getColor().getOtherColor())) {
                // we are targeted by an enemy player, check whether we can beat him
                // if we take his position
                return new Pair<>(IRule.MAX_INCENTIVE / 1000f, new Move(entry.getKey(), upperRight));
            }

            if (context.getBoard().onBoard(upperStraight) && null == upperStraightFigure) {
                return new Pair<>(IRule.MAX_INCENTIVE / 1000f, new Move(entry.getKey(), upperStraight));
            }
        }

        // should not be reached as long as we have stones on the board...
        return null;
    }
}
