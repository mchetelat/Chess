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

import java.util.ArrayList;
import java.util.List;

public final class History {

	private final List<Move> moves;
	private final List<Color> movers;
	
	public History() {
		this.moves = new ArrayList<>();
		this.movers = new ArrayList<>();
	}

	private History(List<Move> moves, List<Color> movers){
		this.moves = moves;
		this.movers = movers;
	}
	
	/**
	 * Adds a new move to the end of the History and returns the new, resulting history.
	 * @param move
	 * @param color
	 * @return
	 */
	public History additionalMove(Move move, Color color){
		List<Move> movesNew = new ArrayList<Move>(this.moves);
		List<Color> moversNew = new ArrayList<>(this.movers);				
		movesNew.add(move);
		moversNew.add(color);
		return new History(movesNew, moversNew);
	}
	
	/**
	 * The number of moves in this history.
	 * @return
	 */
	public int size(){
		return moves.size();
	}
	
	public Move getMove(int index){
		return moves.get(index);
	}
	
	public Color getMover(int index){
		return movers.get(index);
	}
	
	public Color lastMover(){
		return movers.get(movers.size()-1);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i<moves.size(); i++){
			builder.append('(').append(i).append(')');
			builder.append(movers.get(i)).append(':');
			builder.append(moves.get(i).toString());
			builder.append(" ");
		}
		return builder.toString();
	}
	
}
