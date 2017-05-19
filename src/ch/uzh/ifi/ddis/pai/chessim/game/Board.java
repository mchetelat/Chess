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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Board {
	
	public final int height;
	public final int width;
	private final Map<Coordinates, Figure> figures;
	
	public Board(int height, int width, Map<Coordinates, Figure> figures){
		this.figures = Collections.unmodifiableMap(figures);
		this.height = height;
		this.width = width;
		for(Coordinates coord : figures.keySet()){
			if(!this.onBoard(coord)) throw new IllegalArgumentException();
		}
	}

	/**
	 * Removes a figure and returns the resulting board.
	 * @param coordinates
	 * @return
	 */
	public Board removeFigure(Coordinates coordinates){
		Map<Coordinates, Figure> mapCopy = new HashMap<>(figures);
		mapCopy.remove(coordinates);
		return new Board(height, width, mapCopy);
	}
	
	/**
	 * Moves a figure to a new position and returns the resulting board. 
	 * It is not checked, if thats a valid move. If another figure will be beaten in this move, it first has to be removed from the board.
	 * @throws IllegalArgumentException if the destination field is not empty or if there is no figure to be moved at the origin field.
	 * @param origin
	 * @param destination
	 * @return
	 */
	public Board moveFigure(Coordinates origin, Coordinates destination){
		if(figures.containsKey(destination)){
			throw new IllegalArgumentException("Destination Field is already occupied. If capturing other figure, remove captured figure first");
		}
		Figure toMove = figures.get(origin);
		if(toMove == null) {
			throw new IllegalArgumentException("No figure to move");
		}
		Board newBoard = removeFigure(origin);
		Map<Coordinates, Figure> newPositions = new HashMap<>(newBoard.figures());
		newPositions.put(destination, toMove);
		Board result =  new Board(height, width, newPositions);
		return result;
	}
	
	
	/**
	 * Returns all figures on the board
	 * @param color
	 * @return
	 */
	public Map<Coordinates, Figure> figures(){
		//Save Operation as figures is unmodifyable
		return figures;
	}
	
	/**
	 * Returns all figures on the board with a given color
	 * @param color
	 * @return
	 */
	public Map<Coordinates, Figure> figures(Color color){
		Map<Coordinates, Figure> result = new HashMap<Coordinates, Figure>();
		
		for(Map.Entry<Coordinates, Figure> entry : figures.entrySet()){
			if(entry.getValue().color == color){
				result.put(entry.getKey(), entry.getValue());
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the coordinates of a given figure.
	 * null if figure is not on board.
	 * @param figure
	 * @return
	 */
	public Coordinates coordinatesOf(Figure figure){
		for(Map.Entry<Coordinates, Figure> entry : figures.entrySet()){
			if(entry.getValue().equals(figure)){
				return entry.getKey();
			}
		}
		return null;
	}
	
	/**
	 * The figure on this board at the specified coordinates. Returns null if no figure on the field.
	 * @param coordinates
	 * @return
	 */
	public Figure figureAt(Coordinates coordinates){
		return figures.get(coordinates);
	}
	
	/**
	 * Checks if the given coordinates exist on this board.
	 * @param coordinates
	 * @return
	 */
	public boolean onBoard(Coordinates coordinates){
		return coordinates.column >= 0 
				&& coordinates.row >= 0 
				&& coordinates.column < this.width 
				&& coordinates.row < this.height;
	}
			
	
	@Override
	public String toString(){
		StringBuilder figuresString = new StringBuilder();
		for(Map.Entry<Coordinates,Figure> figure : this.figures(Color.WHITE).entrySet()){
			figuresString.append(figure.toString()).append(',');
		}
		figuresString.append("\n");
		for(Map.Entry<Coordinates,Figure> figure : this.figures(Color.BLACK).entrySet()){
			figuresString.append(figure.toString()).append(',');
		}
		if(!this.figures(null).isEmpty()){
			figuresString.append("AND SOME FIGURES WITHOUT COLOR");
		}
		return figuresString.toString();
	}


}
