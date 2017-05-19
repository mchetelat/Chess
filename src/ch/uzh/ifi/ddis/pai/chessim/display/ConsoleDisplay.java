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

import java.util.HashMap;

import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.Pawn;

public class ConsoleDisplay implements Display{

	private static ConsoleDisplay INSTANCE = null;
	private HashMap<String, String> whiteSymbols;
	private HashMap<String, String> blackSymbols;
	
	public ConsoleDisplay(){
		this.whiteSymbols = new HashMap<String, String>();
		whiteSymbols.put(Pawn.TYPE_NAME,"X");
		this.blackSymbols = new HashMap<String, String>();	
		blackSymbols.put(Pawn.TYPE_NAME,"O");
	}
	
	public static ConsoleDisplay getInstance(){
		if (INSTANCE == null){
			INSTANCE = new ConsoleDisplay();
		}
		return INSTANCE;
	}
	
	@Override
	public void display(Board board){
		for(int j = board.height-1; j >= 0; j--){
			System.out.print('|');
			for(int i = 0; i < board.width; i++){
				Figure figureAtPosition = board.figureAt(new Coordinates(j,i));
				if(figureAtPosition == null){
					System.out.print("_");
				}else{
					if(figureAtPosition.color == Color.BLACK){
						System.out.print(blackSymbols.get(figureAtPosition.type));
					}else{
						System.out.print(whiteSymbols.get(figureAtPosition.type));
					}
				}
				System.out.print('|');
			}
			System.out.println();
		}
		System.out.println();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.display.Display#displayRound(int)
	 */
	@Override
	public void moveNumber(int i) {
		System.out.println("Executing Move " + i);
		
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.display.Display#message(java.lang.String)
	 */
	@Override
	public void message(String message) {
		System.out.println("Game Message: " +  message);
		
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.display.Display#winner(ch.uzh.ifi.ddis.pai.chessim.game.Color)
	 */
	@Override
	public void winner(Color winner) {
		System.out.println("Winner: " + winner.toString());
	}

	/* (non-Javadoc)
	 * @see ch.uzh.ifi.ddis.pai.chessim.display.Display#winningReason(java.lang.String)
	 */
	@Override
	public void winningReason(String reason) {
		System.out.println("Winning Reason: " + reason);
	}
}
