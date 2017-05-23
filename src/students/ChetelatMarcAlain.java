package students;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.uzh.ifi.ddis.pai.chessim.game.Agent;
import ch.uzh.ifi.ddis.pai.chessim.game.Board;
import ch.uzh.ifi.ddis.pai.chessim.game.Color;
import ch.uzh.ifi.ddis.pai.chessim.game.Coordinates;
import ch.uzh.ifi.ddis.pai.chessim.game.Figure;
import ch.uzh.ifi.ddis.pai.chessim.game.History;
import ch.uzh.ifi.ddis.pai.chessim.game.Move;
import ch.uzh.ifi.ddis.pai.chessim.game.WinnerRules;
import ch.uzh.ifi.ddis.pai.chessim.game.randomMover.PawnChessWinner;
import students.chetelatmarcalain.game.CustomBoard;

public class ChetelatMarcAlain implements Agent {

	private Move blackNextMove;

	private History history;

	private Color playerColor;

	private WinnerRules rules = new PawnChessWinner();

	private Move whiteNextMove;

	private final int MATERIAL = 50;

	/**
	 * Default C'tor.
	 */
	public ChetelatMarcAlain() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#developerAlias()
	 */
	@Override
	public String developerAlias() {
		return "mac";
	}

	/**
	 * Get all possible moves for a player.
	 * 
	 * @param board
	 *            the board
	 * @param player
	 *            the player color
	 * @return A map containing all possible moves and the board after applying
	 *         a specific move.
	 */
	private Map<Move, Board> getAllPossibleMoves(Board board, Color player) {
		Map<Move, Board> ret = new HashMap<>();
		Map<Coordinates, Figure> pawnList = board.figures(player);
		for (Entry<Coordinates, Figure> pawn : pawnList.entrySet()) {
			ret.putAll(pawn.getValue().possibleMoves(board));
		}

		return ret;
	}

	/**
	 * Return an "infinite" number depending on the color.
	 * 
	 * @param player
	 *            the player color
	 * @return an "infinite" number
	 */
	private int infinite(Color player) {
		switch (player) {
		case WHITE:
			return 10000; // inf
		case BLACK:
			return -10000;
		default:
			return 0;
		}
	}

	/**
	 * Evaluate if a pawn is passed and can not fail anymore.
	 * 
	 * @param board
	 *            the board
	 * @param field
	 *            the pawn's coordinates
	 * @return true if the pawn is safe, else false
	 */
	private boolean isPassedPawn(Board board, Coordinates field) {
		// no pawn can stop it from promoting
		Color opponent = (board.figureAt(field).color).getOtherColor();
		Coordinates coordinates = null;
		switch (opponent) {
		case BLACK:
			for (int row = field.getRow() + 1; row < 8; row++) {
				if (board.figureAt(new Coordinates(row, field.getColumn())) != null) {
					return false;
				}
			}

			if (field.getColumn() > 0) {
				for (int row = field.getRow() + 1; row < 8; row++) {
					coordinates = new Coordinates(row, field.getColumn() - 1);
					if (board.figureAt(coordinates) != null && board.figureAt(coordinates).color == opponent) {
						return false;
					}
				}
			}

			if (field.getColumn() < 7) {
				for (int row = field.getRow() + 1; row < 8; row++) {
					coordinates = new Coordinates(row, field.getColumn() + 1);
					if (board.figureAt(coordinates) != null && board.figureAt(coordinates).color == opponent) {
						return false;
					}
				}
			}
			break;
		case WHITE: // 8 to 0
			for (int row = field.getRow() - 1; row >= 0; row--) {
				if (board.figureAt(new Coordinates(row, field.getColumn())) != null) {
					return false;
				}
			}

			if (field.getColumn() > 0) {
				for (int row = field.getRow() - 1; row >= 0; row--) {
					coordinates = new Coordinates(row, field.getColumn() - 1);
					if (board.figureAt(coordinates) != null && board.figureAt(coordinates).color == opponent) {
						return false;
					}
				}
			}

			if (field.getColumn() < 7) {
				for (int row = field.getRow() - 1; row >= 0; row--) {
					coordinates = new Coordinates(row, field.getColumn() + 1);
					if (board.figureAt(coordinates) != null && board.figureAt(coordinates).color == opponent) {
						return false;
					}
				}
			}
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * Recursive function to simulate the game.
	 * 
	 * @param board
	 *            the board
	 * @param level
	 *            the level
	 * @param player
	 *            the player color
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public int minimax(Board board, int level, Color player, int alpha, int beta) {
		// ending condition
		int nodeScore;
		Color opponent = player.getOtherColor();
		Map<Move, Board> moves = getAllPossibleMoves(board, player);

		if (rules.winner(board, history, player) != null || moves.size() == 0 || level == 5) {
			return scoreCalculation(board, player);
		}

		// alpha beta pruning
		switch (player) {
		case WHITE:
			for (Entry<Move, Board> move : moves.entrySet()) {
				Figure removedFigure = null;

				// if beating opponents figure, remove it
				if (board.figureAt(move.getKey().to) != null && board.figureAt(move.getKey().to).color != player) {
					removedFigure = board.figureAt(move.getKey().to);
					board = board.removeFigure(move.getKey().to);
				}

				// apply move
				board = board.moveFigure(move.getKey().from, move.getKey().to);

				// not calculated everytime, passed from parent to node - time
				// efficient
				nodeScore = minimax(new Board(board.height, board.width, board.figures()), level + 1, opponent, alpha,
						beta);

				// unapply move
				board = board.moveFigure(move.getKey().to, move.getKey().from);
				if (removedFigure != null) {
					CustomBoard customBoard = new CustomBoard(board.height, board.width, board.figures());
					board = customBoard.addFigure(move.getKey().to, removedFigure);
				}

				if (nodeScore > alpha) {
					alpha = nodeScore;
					// update move
					if (level == 0)
						this.whiteNextMove = move.getKey();
				}
				if (alpha >= beta)
					break; // no-need to consider further
			}
			return alpha;
		case BLACK:
			for (Entry<Move, Board> move : moves.entrySet()) {
				Figure removedFigure = null;

				// if beating opponents figure, remove it
				if (board.figureAt(move.getKey().to) != null && board.figureAt(move.getKey().to).color != player) {
					removedFigure = board.figureAt(move.getKey().to);
					board = board.removeFigure(move.getKey().to);
				}

				// apply move
				board = board.moveFigure(move.getKey().from, move.getKey().to);

				nodeScore = minimax(new Board(board.height, board.width, board.figures()), level + 1, opponent, alpha,
						beta);

				board = board.moveFigure(move.getKey().to, move.getKey().from);
				if (removedFigure != null) {
					CustomBoard customBoard = new CustomBoard(board.height, board.width, board.figures());
					board = customBoard.addFigure(move.getKey().to, removedFigure);
				}

				if (nodeScore < beta) {
					beta = nodeScore;
					// update move
					// Take in account that shorter wins are better..
					if (level == 0)
						this.blackNextMove = move.getKey();
				}
				if (alpha >= beta)
					break; // no-need to consider further
			}
			return beta;

		default:
			return Integer.MIN_VALUE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.uzh.ifi.ddis.pai.chessim.game.Agent#nextMove(ch.uzh.ifi.ddis.pai.
	 * chessim.game.Color, ch.uzh.ifi.ddis.pai.chessim.game.Board,
	 * ch.uzh.ifi.ddis.pai.chessim.game.History, long)
	 */
	@Override
	public Move nextMove(Color player, Board board, History history, long timeLimit) {
		this.playerColor = player;
		this.history = history;

		minimax(new Board(board.height, board.width, board.figures()), 0, playerColor, Integer.MIN_VALUE,
				Integer.MAX_VALUE);

		switch (playerColor) {
		case WHITE:
			return whiteNextMove;
		case BLACK:
			return blackNextMove;
		default:
			return null;
		}
	}

	/**
	 * Score calculation of a possible move.
	 * 
	 * @param board
	 *            the board
	 * @param player
	 *            the player color
	 * @return the score
	 */
	private int scoreCalculation(Board board, Color player) {
		Color winner = rules.winner(board, history, player);

		if (winner != null) {
			return infinite(winner);
		}

		int score = 0;
		// White support per column
		int whiteSupport[] = new int[8];
		// Black support per column
		int blackSupport[] = new int[8];
		// Space behind first white pawn (columnwise)
		int whiteSpaceBack[] = new int[8];
		// Space behind first black pawn (columnwise)
		int blackSpaceBack[] = new int[8];
		// Columns which contain white pawn
		boolean whiteColumn[] = new boolean[8];
		// Columns which contain black pawn
		boolean blackColumn[] = new boolean[8];
		// Reflects if a player is one move ahead
		int materialDifference = 0;
		int support;

		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				if (board.figureAt(new Coordinates(row, column)) != null) {
					switch (board.figureAt(new Coordinates(row, column)).color) {
					case WHITE:
						whiteSpaceBack[column] = Math.max(whiteSpaceBack[column], row);
						if (whiteColumn[column])
							score--; // double pawns in column

						// To check how many can block it
						whiteColumn[column] = true;

						whiteSupport[column]++;
						if (column > 0)
							whiteSupport[column - 1]++;
						if (column < 7)
							whiteSupport[column + 1]++;

						materialDifference++;

						// Calculate support points for figure
						support = 0;
						Coordinates coordinates;
						if (column > 0) {
							coordinates = new Coordinates(row + 1, column - 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.BLACK)
								support--;
							coordinates = new Coordinates(row - 1, column - 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.WHITE)
								support++;
						}
						if (column < 7) {
							coordinates = new Coordinates(row + 1, column + 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.BLACK)
								support--;
							coordinates = new Coordinates(row - 1, column + 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.WHITE)
								support++;
						}
						if (support < 0)
							score = score + MATERIAL * support;

						break;
					case BLACK:
						blackSpaceBack[column] = Math.min(blackSpaceBack[column], row - 7);
						if (blackColumn[column])
							score++;
						blackColumn[column] = true;

						blackSupport[column]++;
						if (column > 0)
							blackSupport[column - 1]++;
						if (column < 7)
							blackSupport[column + 1]++;

						materialDifference--;

						support = 0;
						if (column > 0) {
							coordinates = new Coordinates(row - 1, column - 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.WHITE)
								support--;
							coordinates = new Coordinates(row + 1, column - 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.BLACK)
								support++;
						}
						if (column < 7) {
							coordinates = new Coordinates(row - 1, column + 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.WHITE)
								support--;
							coordinates = new Coordinates(row + 1, column + 1);
							if (board.onBoard(coordinates) && board.figureAt(coordinates) != null
									&& board.figureAt(coordinates).color == Color.BLACK)
								support++;
						}
						if (support < 0)
							score = score - MATERIAL * support;

						break;
					default:
						break;
					}
				}
			}
		}

		score = score + MATERIAL * materialDifference;

		// Calculate minimum moves
		// The closer the passed pawn the more points
		// Find column score and aspire winnings pawns with more support
		int minMovesWhite = 1000;
		int minMovesBlack = 1000;
		int supporter;
		for (int i = 0; i < whiteSupport.length; i++) {
			if (whiteSupport[i] > blackSupport[i]) {
				if (i == 0)
					supporter = 7 - whiteSpaceBack[i + 1];
				else if (i == 7)
					supporter = 7 - whiteSpaceBack[i - 1];
				else
					supporter = 7 - Math.max(whiteSpaceBack[i - 1], whiteSpaceBack[i + 1]);

				minMovesWhite = 7 - whiteSpaceBack[i] + supporter - 2;
			} else if (whiteSupport[i] < blackSupport[i]) {
				if (i == 0)
					supporter = blackSpaceBack[i + 1] + 7;
				else if (i == 7)
					supporter = blackSpaceBack[i - 1] + 7;
				else
					supporter = 7 + Math.min(blackSpaceBack[i - 1], blackSpaceBack[i + 1]);
				minMovesBlack = blackSpaceBack[i] + 7 + supporter - 2;
			}
		}

		int whiteBestPassed = -1;
		int blackBestPassed = 100;

		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				if (board.figureAt(new Coordinates(row, column)) != null) {
					switch (board.figureAt(new Coordinates(row, column)).color) {
					case WHITE:
						if (isPassedPawn(board, new Coordinates(row, column))) {
							whiteBestPassed = Math.max(whiteBestPassed, row);
						}
						break;
					case BLACK:
						if (isPassedPawn(board, new Coordinates(row, column))) {
							blackBestPassed = Math.min(blackBestPassed, row);
						}
						break;
					default:
						break;
					}
				}
			}
		}

		if (whiteBestPassed == -1 && blackBestPassed == 100)
			return score;

		whiteBestPassed = 7 - whiteBestPassed;
		if (whiteBestPassed < blackBestPassed && whiteBestPassed < minMovesBlack)
			score += 10000;

		if (blackBestPassed < whiteBestPassed && blackBestPassed < minMovesWhite)
			score -= 10000;

		return score;

	}
}