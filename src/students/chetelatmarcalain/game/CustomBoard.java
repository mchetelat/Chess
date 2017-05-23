package students.chetelatmarcalain.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;

/**
 * A custom board implementation, since the {@link Board} class does
 * not provide the functionality to add a figure.
 * 
 * @author mac
 *
 */
public class CustomBoard {

	public final int height;
	public final int width;
	private final Map<Coordinates, Figure> figures;

	public CustomBoard(int height, int width, Map<Coordinates, Figure> figures) {
		this.figures = Collections.unmodifiableMap(figures);
		this.height = height;
		this.width = width;
	}

	/**
	 * Add a figure to the board
	 * 
	 * @param coordinates the coordinates the figure is set to
	 * @param figureToAdd the figure
	 * @return the board
	 */
	public Board addFigure(Coordinates coordinates, Figure figureToAdd) {
		Map<Coordinates, Figure> mapCopy = new HashMap<>(figures);
		mapCopy.put(coordinates, figureToAdd);
		return new Board(8, 8, mapCopy);
	}
}