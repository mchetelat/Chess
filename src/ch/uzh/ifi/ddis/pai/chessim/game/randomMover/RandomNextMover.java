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

import java.util.Random;

import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.NextMoverRules;

public final class RandomNextMover implements NextMoverRules {
	
	private final Random rng;
	public final double probabilitySameMover;

	public RandomNextMover(long seed, double probabilitySameMover){
		rng = new Random(seed);
		this.probabilitySameMover = probabilitySameMover;
	}
	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.NextMoverRules#nextMover(ch.uzh.ifi.ddis.pai.chessim.game.History)
	 */
	@Override
	public Color nextMover(History history) {
		if(history.size() == 0){
			//White moves first
			return Color.WHITE;
		}
		Color lastMover = history.getMover(history.size()-1);
		
		if(rng.nextDouble() <= probabilitySameMover){
			return lastMover;
		}else {
			return lastMover.getOtherColor();
		}
	}

}
