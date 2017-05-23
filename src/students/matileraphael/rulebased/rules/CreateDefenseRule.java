package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tries to increase the defense of particular positions by evaluating, whether a straight move forward
 * will result in a better position or not.
 */
public class CreateDefenseRule extends ADefenseVectorRule implements IRule {

    @Override
    public Pair<Float, Move> getMove(Context context) {
        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        List<Pair<Float, Move>> positionsToBuildDefenseOn = new ArrayList<>();
        float min = 0;
        float max = 0;

        for (Map.Entry<Coordinates, Figure> entry : context.getBoard().figures(context.getColor()).entrySet()) {
            // check whether we can move one step ahead and our defense gets increased strength
            Pair<Float, Move> increasedDefenseMove = this.stepAheadWithIncreasedDefense(entry.getKey(), context.getBoard(), context.getColor(), directionFactor);

            if (null != increasedDefenseMove) {
                positionsToBuildDefenseOn.add(increasedDefenseMove);

                if (increasedDefenseMove.getKey() > max) {
                    max = increasedDefenseMove.getKey();
                }

                if (increasedDefenseMove.getKey() < min) {
                    min = increasedDefenseMove.getKey();
                }
            }
        }

        Collections.sort(positionsToBuildDefenseOn);

        // normalize values to [0,1] before returning
        for (Pair<Float, Move> entry : positionsToBuildDefenseOn) {
            float normalized = (entry.getKey() - min) / (max - min);
            entry.setKey(normalized * 0.70f);
        }

        if (positionsToBuildDefenseOn.size() > 0) {
            return positionsToBuildDefenseOn.get(positionsToBuildDefenseOn.size() - 1);
        }

        return null;
    }

    private Pair<Float, Move> stepAheadWithIncreasedDefense(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {
        Coordinates straightAhead = new Coordinates(startingPosition.getRow() + (directionIndicator), startingPosition.getColumn());

        if (board.onBoard(straightAhead) && null == board.figureAt(straightAhead)) {
            int origDefense = this.countDefenders(startingPosition, board, ownColor, directionIndicator);
            int newPositionDefense = this.countDefenders(straightAhead, board, ownColor, directionIndicator);

            if (newPositionDefense > origDefense) {
                return new Pair<>((float) (newPositionDefense - origDefense), new Move(startingPosition, straightAhead));
            }
        }

        return null;
    }
}
