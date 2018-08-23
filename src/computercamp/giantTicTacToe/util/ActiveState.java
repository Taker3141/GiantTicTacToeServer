package computercamp.giantTicTacToe.util;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class ActiveState
{
	public CellState[][] board = new CellState[9][9];
	public CellState[][] bigBoard = new CellState[3][3];
	public boolean myTurn = false;
	public int activeX = 3, activeY = 3;
	public CellState symbol = null;
	public boolean initialized = false;
	public boolean done = false;
}
