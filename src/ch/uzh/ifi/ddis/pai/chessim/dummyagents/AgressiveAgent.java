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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import ch.uzh.ifi.ddis.pai.chessim.game.Agent;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;

public class AgressiveAgent implements Agent {

	private Random rng;
	
	public AgressiveAgent(){
		this.rng = new Random();
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#nextMove(ch.uzh.ifi.ddis.pai.chessim.game.Color, ch.uzh.ifi.ddis.pai.chessim.game.Board, ch.uzh.ifi.ddis.pai.chessim.game.History, long)
	 */
	@Override
	public Move nextMove(Color yourColor, Board board, History history,
			long timeLimit) {
		long timeSecurityMargin = timeLimit > 1200 ? 300 : timeLimit / 4;
		long startTime = new Date().getTime();
		List<Figure> figures = new ArrayList<Figure>(board.figures(yourColor).values());
		Collections.shuffle(figures);
		Iterator<Figure> figuresIter = figures.iterator();
		List<Move> possibleMoves = new ArrayList<>();
		Move reserveMove = null;
		while(new Date().getTime()-startTime < timeLimit - timeSecurityMargin && figuresIter.hasNext()){
			Map<Move, Board> moves = figuresIter.next().possibleMoves(board);
			for(Entry<Move, Board> move : moves.entrySet()){
				if(board.figureAt(move.getKey().to) != null){
					possibleMoves.add(move.getKey());
				}else if(reserveMove == null){
					reserveMove = move.getKey(); 
				}
			}
		}
		if(!possibleMoves.isEmpty()){
			return possibleMoves.get(rng.nextInt(possibleMoves.size()));
		}else{
			return reserveMove;
		}
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#developerAlias()
	 */
	@Override
	public String developerAlias() {
		return this.getClass().getSimpleName(); 
	}

}
