package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.*;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract rule, specifying helper methods to find defenders resp. attackers
 * for a particular pawn position. In addition, such states are cached, to increase
 * speed if multiple implementations of {@link ICachingRule} will use these methods.
 * <p>
 * Due to the changing game state on every move, the cache has to be rebuilt every time.
 */
public abstract class ADefenseVectorRule implements ICachingRule {

    protected final Map<Coordinates, Integer> defenders = new HashMap<>();
    protected final Map<Coordinates, Integer> attackers = new HashMap<>();

    @Override
    public abstract Pair<Float, Move> getMove(Context context);

    @Override
    public void clearCache() {
        this.defenders.clear();
        this.attackers.clear();
    }

    final protected int countDefenders(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {
        if (this.defenders.containsKey(startingPosition)) {
            return this.defenders.get(startingPosition);
        }

        int defenders = 0;

        Coordinates lowerLeft = new Coordinates(startingPosition.getRow() - (directionIndicator), startingPosition.getColumn() - 1);
        Figure lowerLeftFigure = board.figureAt(lowerLeft);
        Coordinates lowerRight = new Coordinates(startingPosition.getRow() - (directionIndicator), startingPosition.getColumn() + 1);
        Figure lowerRightFigure = board.figureAt(lowerRight);

        if (null != lowerLeftFigure && lowerLeftFigure.color.equals(ownColor)) {
            defenders++;
            defenders += this.countDefenders(lowerLeft, board, ownColor, directionIndicator);
        }

        if (null != lowerRightFigure && lowerRightFigure.color.equals(ownColor)) {
            defenders++;
            defenders += this.countDefenders(lowerRight, board, ownColor, directionIndicator);
        }

        this.defenders.put(startingPosition, defenders);

        return defenders;
    }

    final protected int countAttackers(Coordinates startingPosition, Board board, Color ownColor, int directionIndicator) {
        if (this.attackers.containsKey(startingPosition)) {
            return this.attackers.get(startingPosition);
        }

        int attackers = 0;

        Coordinates upperLeft = new Coordinates(startingPosition.getRow() + (directionIndicator), startingPosition.getColumn() - 1);
        Figure upperLeftFigure = board.figureAt(upperLeft);
        Coordinates upperRight = new Coordinates(startingPosition.getRow() + (directionIndicator), startingPosition.getColumn() + 1);
        Figure upperRightFigure = board.figureAt(upperRight);

        if (null != upperLeftFigure && upperLeftFigure.color.equals(ownColor)) {
            attackers++;
            attackers += this.countAttackers(upperLeft, board, ownColor, directionIndicator);
        }

        if (null != upperRightFigure && upperRightFigure.color.equals(ownColor)) {
            attackers++;
            attackers += this.countAttackers(upperRight, board, ownColor, directionIndicator);
        }

        this.attackers.put(startingPosition, attackers);

        return attackers;
    }
}
