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

public interface Agent {

	/**
	 * The pseudonym of this agent, 
	 * which will be used when publishing the result of the chess tournament.
	 * 
	 * You can of course use your real name, if you want.
	 * @return
	 */
	String developerAlias();
	
	/**
	 * The central method of the chess agent. Contains the AI to determine the next move.
	 * @param yourColor
	 * @param board: The current state of the game
	 * @param history: The previous moves of this game
	 * @param timeLimit: The amount of time (in ms) you have to return a solution. 
	 * @return
	 */
	Move nextMove(Color yourColor, Board board, History history, long timeLimit);
}
