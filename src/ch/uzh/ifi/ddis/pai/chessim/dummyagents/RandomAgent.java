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
package ch.uzh.ifi.ddis.pai.chessim.dummyagents;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ch.uzh.ifi.ddis.pai.chessim.game.Agent;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;

public class RandomAgent implements Agent {	

	private Random rng;	
	
	public RandomAgent(){
		this.rng = new Random();
	}
	
	public RandomAgent(long seed){
		this.rng = new Random(seed);
	}
	
	
	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#nextMove(ch.uzh.ifi.ddis.pai.chessim.game.Color, ch.uzh.ifi.ddis.pai.chessim.game.Board, ch.uzh.ifi.ddis.pai.chessim.game.History, long)
	 */
	@Override
	public Move nextMove(Color yourColor, Board board, History history,
			long timeLimit) {
		Iterator<Figure> figures = board.figures(yourColor).values().iterator();
		List<Move> possibleMoves = new ArrayList<>();
		while(figures.hasNext()){
			possibleMoves.addAll(figures.next().possibleMoves(board).keySet());
		}
		return possibleMoves.get(rng.nextInt(possibleMoves.size()));		
	}


	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#developerAlias()
	 */
	@Override
	public String developerAlias() {
		return this.getClass().getSimpleName();
	}

}
