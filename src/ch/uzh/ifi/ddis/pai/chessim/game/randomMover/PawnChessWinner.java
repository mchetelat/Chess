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

import java.util.Collection;
import java.util.Map;

import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.Pawn;
import ch.uzh.ifi.ddis.pai.chessim.game.WinnerRules;

public class PawnChessWinner implements WinnerRules {

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.WinnerRules#winner(ch.uzh.ifi.ddis.pai.chessim.game.Board, ch.uzh.ifi.ddis.pai.chessim.game.History, ch.uzh.ifi.ddis.pai.chessim.game.Color)
	 */
	@Override
	public Color winner(Board board, History history, Color nextMover) {
		if(nextMover == null || history == null || board == null){
			throw new IllegalArgumentException();
		}
		
		// Check if someone completed the game (moved a pawn to the other side of the board)
		Map<Coordinates, Figure> figures = board.figures();
		for(Map.Entry<Coordinates, Figure> entry : figures.entrySet()){
			if(entry.getValue().type.equalsIgnoreCase(Pawn.TYPE_NAME)){
				if(entry.getValue().color == Color.WHITE && entry.getKey().getRow() == board.height-1){
					return Color.WHITE;
				}else if(entry.getValue().color == Color.BLACK && entry.getKey().getRow() == 0){
					return Color.BLACK;
				}
			}
		}
		
		// Check if next mover can't move anymore
		Collection<Figure> moversFigures = board.figures(nextMover).values();
		Color otherColor = nextMover == Color.BLACK ? Color.WHITE : Color.BLACK;
		if(moversFigures.isEmpty()){
			return otherColor;
		}else{
			for(Figure figure : moversFigures){
				if(!figure.possibleMoves(board).isEmpty()){
					//Next player has at least one possible move
					return null;
				}
			}
			//Next player has not possible moves
			return otherColor;
		}

	}

}
