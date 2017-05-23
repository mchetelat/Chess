package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.*;

/**
 * A rule which its only purpose lies in returning a straight move
 * for a pawn, if the new position has any defenders.
 */
public class StraightForwardRule implements IRule {

    private Map<Coordinates, Boolean> defenders = new HashMap<>();

    @Override
    public Pair<Float, Move> getMove(Context context) {
        List<Pair<Float, Move>> possibleMoves = new ArrayList<>();

        Map<Coordinates, Figure> figures = context.getBoard().figures(context.getColor());

        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        for (Map.Entry<Coordinates, Figure> entry : figures.entrySet()) {
            // check coordinates on left and right above current position
            Coordinates upperStraight = new Coordinates(entry.getKey().getRow() + (directionFactor), entry.getKey().getColumn());
            Figure upperStraightFigure = context.getBoard().figureAt(upperStraight);

            if (context.getBoard().onBoard(upperStraight) && null == upperStraightFigure) {
                boolean hasDefenders = this.hasDefenders(upperStraight, context.getBoard(), context.getColor(), directionFactor);

                if (hasDefenders) {
                    possibleMoves.add(new Pair<>(IRule.MAX_INCENTIVE / 2f, new Move(entry.getKey(), upperStraight)));
                }
            }
        }

        this.defenders.clear();

        if (possibleMoves.size() > 0) {
            Collections.sort(possibleMoves);
            return possibleMoves.get(possibleMoves.size() - 1);
        }

        return null;
    }

    private boolean hasDefenders(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {
        if (this.defenders.containsKey(startingPosition)) {
            return this.defenders.get(startingPosition);
        }

        Coordinates lowerLeft = new Coordinates(startingPosition.getRow() - (directionIndicator), startingPosition.getColumn() - 1);
        Figure lowerLeftFigure = board.figureAt(lowerLeft);
        Coordinates lowerRight = new Coordinates(startingPosition.getRow() - (directionIndicator), startingPosition.getColumn() + 1);
        Figure lowerRightFigure = board.figureAt(lowerRight);

        boolean hasDefenders = false;

        if (null != lowerLeftFigure && lowerLeftFigure.color.equals(ownColor)) {
            hasDefenders = true;
        }

        if (null != lowerRightFigure && lowerRightFigure.color.equals(ownColor)) {
            hasDefenders = true;
        }

        this.defenders.put(startingPosition, hasDefenders);

        return hasDefenders;
    }

}
