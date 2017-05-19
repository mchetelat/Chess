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
package ch.uzh.ifi.ddis.pai.chessim.display;

import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;

public interface Display {

	/**
	 * The current position of all figures on the board, as well as the board size
	 * @param board
	 */
	public void display(Board board);

	/**
	 * The current move
	 * Note that, as opposed to regular chess notation, every move from any 
	 * agent increases the moveNumber by 1.
	 */
	public void moveNumber(int i);

	/**
	 * Displays the reason the winner won the game
	 * @param string
	 */
	public void winningReason(String reason);
	
	/**
	 * Displays the winner of a game
	 * @param winner
	 */
	public void winner(Color winner);
	
	/**
	 * Any message that should be displayed on the screen that doesn't fit any of the other methods.
	 * @param string
	 */
	public void message(String message);
	
}
