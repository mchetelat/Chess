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


public final class Move {

	public final Coordinates from;
	public final Coordinates to;
	
	
	public Move(Coordinates from, Coordinates to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString(){
		return new StringBuilder().append(from.column).append(',').append(from.row).append("-->").append(to.column).append(',').append(to.row).toString();
	}
	
	@Override
	public boolean equals(Object o) {
	    if(o == null || getClass() != o.getClass()){
	        return false;
	    }else{
	        Move otherMove = (Move) o;
	        return from.equals(otherMove.from) &&  to.equals(otherMove.to);
	    }
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * from.hashCode();
		result = prime * to.hashCode();
		return result;
	}
	
}
