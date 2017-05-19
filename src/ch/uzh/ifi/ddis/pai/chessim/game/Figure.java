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
package ch.uzh.ifi.ddis.pai.chessim.game;

import java.util.Map;

public abstract class Figure {

	public final Color color;
	public final String type;
		
	protected Figure(Color color, String type, char symbol){
		if(color == null) throw new IllegalArgumentException("Color of figure must not be null");
		this.color = color;
		this.type = type;
	}
	
	/**
	 * Returns all the moves a figure can do on a given board, including the resulting new board.
	 * @param currentBoard
	 * @return
	 */
	public abstract Map<Move,Board> possibleMoves(Board currentBoard);
	
}
