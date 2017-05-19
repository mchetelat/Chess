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

import java.util.HashMap;
import java.util.Map;

public final class Pawn extends Figure {
	
	public static final String TYPE_NAME = "Pawn";
	private static char symbol = 'P';

	/**
	 * @param color
	 * @param type
	 * @param symbol
	 */
	public Pawn(Color color) {
		super(color, TYPE_NAME, symbol);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Figure#possibleMoves(ch.uzh.ifi.ddis.pai.chessim.game.Board)
	 */
	@Override
	public Map<Move, Board> possibleMoves(Board currentBoard) {
		int oppositeSideFactor = color == Color.WHITE ? 1 : -1;
		
		Coordinates current = currentBoard.coordinatesOf(this); 
		if(current == null) throw new IllegalArgumentException("The board does not contain this figure");
		
		Map<Move, Board> possibleMoves = new HashMap<>();
		//straight moving coordinates
		Coordinates targetStraight = new Coordinates(current.getRow() + oppositeSideFactor, current.getColumn());
		if(currentBoard.onBoard(targetStraight) 
				&& currentBoard.figureAt(targetStraight) == null){
			//Straight move is possible
			Board newBoard = currentBoard.moveFigure(current, targetStraight);
			possibleMoves.put(new Move(current, targetStraight), newBoard);
		}
		
		//Left-Forward beating coordinates
		Coordinates targetLeft = new Coordinates(current.getRow() + oppositeSideFactor, current.getColumn()-1);
		if(currentBoard.onBoard(targetLeft) 
				&& currentBoard.figureAt(targetLeft) != null 
				&& currentBoard.figureAt(targetLeft).color != color){
			//Left-Forward move is possible
			Board newBoard = currentBoard.removeFigure(targetLeft);
			newBoard = newBoard.moveFigure(current, targetLeft);
			possibleMoves.put(new Move(current, targetLeft), newBoard);
		}
		
		//Right-Forward beating coordinates
		Coordinates targetRight = new Coordinates(current.getRow() + oppositeSideFactor, current.getColumn()+1);
		if(currentBoard.onBoard(targetRight) 
				&& currentBoard.figureAt(targetRight) != null 
				&& currentBoard.figureAt(targetRight).color != color){
			//Right-Forward move is possible
			Board newBoard = currentBoard.removeFigure(targetRight);
			newBoard = newBoard.moveFigure(current, targetRight);
			possibleMoves.put(new Move(current, targetRight), newBoard);
		}
	
		return possibleMoves;
	}

	@Override
	public String toString(){
		String color;
		if(this.color == Color.WHITE){
			color = "W";
		}else if(this.color == Color.BLACK){
			color = "B";
		}else{
			color = "?";
		}
		return String.valueOf(symbol).concat(color);
	}
}
