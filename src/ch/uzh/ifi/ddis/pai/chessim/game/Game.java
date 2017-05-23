/**
 * This file is part of the chess assignment of the "Practical Artificial Intelligence" class at University of Zurich.
 * @copyright:
 *	 Dynamic and Distributed Information Systems Group
 * 	 Departement of Informatics, University of Zurich
 * @author:
 *   Michael Weiss, mail@mweiss.ch
 * @year: 
 *   2016
 */
package ch.uzh.ifi.ddis.pai.chessim.game;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.uzh.ifi.ddis.pai.chessim.display.Display;

public abstract class Game {
	
	private final Map<Color, Agent> agents = new HashMap<>();
	private final WinnerRules winnerRules;
	private final NextMoverRules nextMoverRules;
	private final Board initialBoard;
	public final long timeLimit;
	public final long timeInc;
	private final Set<Display> displays;
	
	
	protected Game(WinnerRules winnerRules, NextMoverRules nextMoverRules, Board initialBoard,
			Agent white, Agent black, long timeLimit, long timeInc){
		agents.put(Color.BLACK, black);
		agents.put(Color.WHITE, white);
		this.nextMoverRules = nextMoverRules;
		this.winnerRules = winnerRules;	
		this.initialBoard = initialBoard;
		this.timeLimit = timeLimit;
		this.timeInc = timeInc;
		this.displays = new HashSet<>();
	}
	
	public GameResult playGame(){
		
		boolean gameRunning = true;
		Board currentBoard = initialBoard;
		History history = new History();
		Color winnersColor = null;
		Map<Color, Long> remainingTimes = new HashMap<Color, Long>();
		remainingTimes.put(Color.WHITE, timeLimit);
		remainingTimes.put(Color.BLACK, timeLimit);
		
		while (gameRunning){
			
			// Determines the next player to make a move
			Color nextMover = nextMoverRules.nextMover(history);
			
			// Determines if game has a Winner
			winnersColor = winnerRules.winner(currentBoard, history, nextMover);
			if(winnersColor != null){
				gameRunning = false;
				for(Display display : displays){
					display.winningReason("Regular Win according to game rules (either reached other side or other player is blocked)");
				}
			}else{
				for(Display display : displays){
					System.out.println(nextMover);
					display.moveNumber(history.size());
				}
				
				try{
					// Ask agent for next move
					long remainingTime = remainingTimes.get(nextMover) + timeInc;
					long timestamp = new Date().getTime();
					Move move = agents.get(nextMover).nextMove(nextMover, currentBoard, history, remainingTime);
					long calculationTime = new Date().getTime() - timestamp; 
					remainingTime -= calculationTime;
					if(remainingTime < -3){
						for(Display display : displays){
							display.winningReason("Player ran out of time. Player disqualified.");
						}
						winnersColor = nextMover.getOtherColor();
						gameRunning = false;
					}else{
						remainingTimes.put(nextMover, remainingTime);
						// Check validity of move
						if(!currentBoard.figureAt(move.from).possibleMoves(currentBoard).containsKey(move)){
							for(Display display : displays){
								display.winningReason("Invalid Move. Player disqualified.");
							}
							winnersColor = nextMover.getOtherColor();
							gameRunning = false;
						}else{
							// Execute Move
							// Note: It is checked at the begin of the next 
							// 		iteration if this is a winning move)
							currentBoard = currentBoard.removeFigure(move.to);
							currentBoard = currentBoard.moveFigure(move.from, move.to);	
							for(Display display : displays){
								display.display(currentBoard);
							}
				 			history = history.additionalMove(move, nextMover);	
						}				
					}
				}catch(Exception e){
					e.printStackTrace();
					winnersColor = nextMover.getOtherColor();
					for(Display display : displays){
						// Note that, for every player which gets disqualified because of a thrown exception, 
						// we will manually check if the exception was indeed caused by the agent. 
						// Otherwise, he will, of course, not get disqualified.
						display.message("Exception thrown while calculating move. Player disqualified");
					}
					break;
				}		
			}
		}
		for(Display display : displays){
			display.winner(winnersColor);
		}
		
		GameResult result = new GameResult(history, agents.get(winnersColor), winnersColor);
		return result;
		
	}
	
	public void registerDisplay(Display display){
		displays.add(display);
	}
	
	public void removeDisplay(Display display){
		displays.remove(display);
	}
	
	public Set<Display> getDisplays(){
		return Collections.unmodifiableSet(displays);
	}
	
	/**
	 * A class summarizing the outcome of a game
	 */
	public final class GameResult{
		
		private final History history;
		private final Agent winner;
		private final Color winnersColour;
		
		public GameResult(History history, Agent winner, Color winnersColour) {
			super();
			this.history = history;
			this.winner = winner;
			this.winnersColour = winnersColour;
		}

		/**
		 * @return the history
		 */
		public History getHistory() {
			return history;
		}

		/**
		 * @return the winner
		 */
		public Agent getWinner() {
			return winner;
		}

		/**
		 * @return the winnersColour
		 */
		public Color getWinnersColour() {
			return winnersColour;
		}
			
		
	}
}

