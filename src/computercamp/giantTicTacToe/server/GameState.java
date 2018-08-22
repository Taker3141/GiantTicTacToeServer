package computercamp.giantTicTacToe.server;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class GameState
{
	public final CellState[][] board;
	public final CellState[][] bigBoard;
	public final int activeX, activeY;
	
	public GameState(CellState[][] board, CellState[][] bigBoard, int activeX, int activeY)
	{
		this.board = board;
		this.bigBoard = bigBoard;
		this.activeX = activeX;
		this.activeY = activeY;
	}
}
