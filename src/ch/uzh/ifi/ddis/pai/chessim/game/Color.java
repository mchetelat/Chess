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

public enum Color {
	BLACK, WHITE;
	
	private Color other;

    static {
    	BLACK.other = WHITE;
    	WHITE.other = BLACK;
    }

    public Color getOtherColor() {
        return other;
    }

}
