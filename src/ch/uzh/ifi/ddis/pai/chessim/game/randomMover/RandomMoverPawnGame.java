/**
 * This file is part of the chess assignment of the 
 * "Practical Artificial Intelligence" class at University of Zurich.
 *
 * @copyright:
 *	 Dynamic and Distributed Information Systems Group
 * 	 Departement of Informatics, University of Zurich
 * @author:
 *   Michael Weiss, mail@mweiss.ch
 * @year: 
 *   2016
 */
package ch.uzh.ifi.ddis.pai.chessim.game.randomMover;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.ifi.ddis.pai.chessim.game.Agent;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.Game;
import ch.uzh.ifi.ddis.pai.chessim.game.Pawn;

public class RandomMoverPawnGame extends Game {

	public RandomMoverPawnGame(long timeLimit, long timeInc, Agent white, Agent black, double probabilitySameMover,
			long seed) {
		super(new PawnChessWinner(), new RandomNextMover(seed, probabilitySameMover), createNewBoard(), white, black,
				timeLimit, timeInc);
	}

	/**
	 * Creates a new 8x8 board with 16 pawns per color
	 * 
	 * @return
	 */
	private static Board createNewBoard() {
		Map<Coordinates, Figure> figures = new HashMap<>();
		int quadraticBoardSize = 8;
		for (int row = 0; row <= 1; row++) {
			for (int column = 0; column < quadraticBoardSize; column++) {
				figures.put(new Coordinates(row, column), new Pawn(Color.WHITE));
			}
		}
		for (int row = quadraticBoardSize - 2; row <= quadraticBoardSize - 1; row++) {
			for (int column = 0; column < quadraticBoardSize; column++) {
				figures.put(new Coordinates(row, column), new Pawn(Color.BLACK));
			}
		}
		// figures.put(new Coordinates(0, 3), new Pawn(Color.WHITE));
		// figures.put(new Coordinates(3, 5), new Pawn(Color.WHITE));
		// figures.put(new Coordinates(4, 3), new Pawn(Color.BLACK));
		// figures.put(new Coordinates(7, 4), new Pawn(Color.BLACK));

		return new Board(quadraticBoardSize, quadraticBoardSize, figures);
	}
}
