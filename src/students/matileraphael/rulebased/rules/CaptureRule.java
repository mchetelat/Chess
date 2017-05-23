package students.matileraphael.rulebased.rules;

import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import students.matileraphael.commons.Context;
import students.matileraphael.commons.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Evaluates the current game state for particular moves which capture enemies.
 * This is mainly based on the decision, which of the both players has a better proportional relation
 * between attackers and defenders for a particular position.
 */
public class CaptureRule extends ADefenseVectorRule implements IRule {

    @Override
    public Pair<Float, Move> getMove(Context context) {
        List<Pair<Float, Move>> possibleMoves = new ArrayList<>();

        Map<Coordinates, Figure> figures = context.getBoard().figures(context.getColor());
        float min = 0;
        float max = 0;

        // white starts from bottom of field
        int directionFactor = context.getColor().equals(Color.WHITE) ? 1 : - 1;

        for (Map.Entry<Coordinates, Figure> entry : figures.entrySet()) {
            // check coordinates on left and right above current position
            Coordinates upperLeft = new Coordinates(entry.getKey().getRow() + (directionFactor), entry.getKey().getColumn() - 1);
            Figure upperLeftFigure = context.getBoard().figureAt(upperLeft);

            Coordinates upperRight = new Coordinates(entry.getKey().getRow() + (directionFactor), entry.getKey().getColumn() + 1);
            Figure upperRightFigure = context.getBoard().figureAt(upperRight);

            if (context.getBoard().onBoard(upperLeft)
                    && null != upperLeftFigure
                    && upperLeftFigure.color.equals(context.getColor().getOtherColor())) {
                // we are targeted by an enemy player, check whether we can beat him
                // if we take his position
                int attackers = this.countAttackers(upperLeft, context.getBoard(), context.getColor(), directionFactor);
                int defenders = this.countDefenders(upperLeft, context.getBoard(), context.getColor(), directionFactor);

                float incentive = (float) (defenders - attackers);

                if (incentive > max) {
                    max = incentive;
                } else if (incentive < min) {
                    min = incentive;
                }

                possibleMoves.add(new Pair<>(incentive, new Move(entry.getKey(), upperLeft)));
            }

            if (context.getBoard().onBoard(upperRight)
                    && null != upperRightFigure
                    && upperRightFigure.color.equals(context.getColor().getOtherColor())) {
                // we are targeted by an enemy player, check whether we can beat him
                // if we take his position
                int attackers = this.countAttackers(upperLeft, context.getBoard(), context.getColor(), directionFactor);
                int defenders = this.countDefenders(upperLeft, context.getBoard(), context.getColor(), directionFactor);

                float incentive = (float) (defenders - attackers);

                if (incentive > max) {
                    max = incentive;
                } else if (incentive < min) {
                    min = incentive;
                }

                possibleMoves.add(new Pair<>(incentive, new Move(entry.getKey(), upperRight)));
            }
        }

        super.attackers.clear();
        super.defenders.clear();

        // normalize values to [0,1] before returning
        for (Pair<Float, Move> entry : possibleMoves) {
            float normalized = (entry.getKey() - min) / (max - min);

            // we attack if equal number of attackers and defenders
            if (normalized == 0) {
                normalized = 1;
            }

            entry.setKey(normalized * 0.95f);
        }

        if (possibleMoves.size() > 0) {
            Collections.sort(possibleMoves);
            return possibleMoves.get(possibleMoves.size() - 1);
        }

        return null;
    }
}
