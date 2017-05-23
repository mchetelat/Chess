package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IncreaseAttackedPawnsDefenseRule extends ADefenseVectorRule implements IRule {

    @Override
    public Pair<Float, Move> getMove(Context context) {
        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        List<Pair<Integer, Coordinates>> positionsToHardlyDefend = new ArrayList<>();

        for (Map.Entry<Coordinates, Figure> entry : context.getBoard().figures(context.getColor()).entrySet()) {
            int defenders = super.countDefenders(entry.getKey(), context.getBoard(), context.getColor(), directionFactor);
            int attackers = super.countAttackers(entry.getKey(), context.getBoard(), context.getColor(), directionFactor);

            // try to be always one move ahead
            if (defenders + 1 <= attackers) {
                positionsToHardlyDefend.add(new Pair<>(attackers - defenders, entry.getKey()));
            }
        }

        Collections.sort(positionsToHardlyDefend);

        for (Pair<Integer, Coordinates> entry : positionsToHardlyDefend) {
            Move defenderMove = this.findAdditionalDefendingMove(entry.getValue(), context.getBoard(), context.getColor(), directionFactor);

            if (null != defenderMove) {
                return new Pair<>(IRule.MAX_INCENTIVE * 0.75f, defenderMove);
            }
        }

        return null;

    }

    private Move findAdditionalDefendingMove(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {
        Coordinates lowerLeft = new Coordinates(startingPosition.getRow() - 2 * (directionIndicator), startingPosition.getColumn() - 1);
        Figure lowerLeftFigure = board.figureAt(lowerLeft);
        Coordinates lowerRight = new Coordinates(startingPosition.getRow() - 2 * (directionIndicator), startingPosition.getColumn() + 1);
        Figure lowerRightFigure = board.figureAt(lowerRight);

        if (null != lowerLeftFigure && lowerLeftFigure.color.equals(ownColor)) {
            Coordinates lowerLeftCloseToStart = new Coordinates(startingPosition.getRow() - (directionIndicator), startingPosition.getColumn() - 1);
            if (null == board.figureAt(lowerLeftCloseToStart)) {
                return new Move(lowerLeft, lowerLeftCloseToStart);
            }
        }

        if (null != lowerRightFigure && lowerRightFigure.color.equals(ownColor)) {
            Coordinates lowerRightCloseToStart = new Coordinates(startingPosition.getRow() - (directionIndicator), startingPosition.getColumn() + 1);
            if (null == board.figureAt(lowerRightCloseToStart)) {
                return new Move(lowerRight, lowerRightCloseToStart);
            }
        }

        return null;
    }
}
