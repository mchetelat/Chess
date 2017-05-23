package students.matileraphael.commons;

import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.History;

public class Context {

    private Color   color;
    private Board   board;
    private History history;

    public Context(Color color, Board board, History history) {
        this.color = color;
        this.board = board;
        this.history = history;
    }

    public Color getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public History getHistory() {
        return history;
    }
}
