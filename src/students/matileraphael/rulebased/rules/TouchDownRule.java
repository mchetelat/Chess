package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A rule which returns moves representing a step forward in order to make a touch down to the other side of the board.
 */
public class TouchDownRule implements IRule {

    @Override
    public Pair<Float, Move> getMove(Context context) {
        List<Pair<Float, Move>> possibleMoves = new ArrayList<>();

        Map<Coordinates, Figure> figures = context.getBoard().figures(context.getColor());

        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        for (Map.Entry<Coordinates, Figure> entry : figures.entrySet()) {
            Coordinates upperStraight = new Coordinates(entry.getKey().getRow() + directionFactor, entry.getKey().getColumn());

            if (context.getBoard().onBoard(upperStraight) && context.getBoard().figureAt(upperStraight) == null) {
                boolean touchDownPossible = this.touchDownPossible(entry.getKey(), context.getBoard(), context.getColor(), directionFactor);

                if (touchDownPossible) {
                    possibleMoves.add(new Pair<>((float) IRule.MAX_INCENTIVE, new Move(entry.getKey(), upperStraight)));
                }
            }
        }

        if (possibleMoves.size() > 0) {
            Collections.sort(possibleMoves);
            return possibleMoves.get(possibleMoves.size() - 1);
        }

        return null;
    }

    private boolean touchDownPossible(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {
        boolean touchDownPossible = true;

        for (int i = startingPosition.getRow(); (i < board.height && i >= 0); i = i + directionIndicator) {
            Coordinates target = new Coordinates(i, startingPosition.getColumn());

            if (! board.onBoard(target) || board.figureAt(target) != null) {
                touchDownPossible = false;
                break;
            }
        }

        return touchDownPossible;
    }
}
