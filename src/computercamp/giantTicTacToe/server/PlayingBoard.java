package computercamp.giantTicTacToe.server;

import static computercamp.giantTicTacToe.util.ErrorCode.*;

import computercamp.giantTicTacToe.util.ErrorCode;

public class PlayingBoard
{
	CellState[][] board = new CellState[9][9];
	CellState[][] bigBoard = new CellState[3][3];
	int activeX = 3, activeY = 3;
	
	public ErrorCode setCell(int x, int y, CellState s)
	{
		ErrorCode status = checkCoordinates(x, y);
		if(status == NO_ERROR)
		{
			board[x][y] = s;
			calculateBigBoardState();
			calculateNextActiveField(x, y);
		}
		else System.out.println("Invalid move");
		return status;
	}
	
	private void calculateBigBoardState()
	{
		for(int startX = 0; startX < 9; startX += 3) for(int startY = 0; startY < 9; startY += 3)
		{
			boolean boardFull = true;
			//Check rows and columns
			for(int j = 0; j < 3; j++)
			{
				boolean rowFull = true;
				boolean columnFull = true;
				CellState symbolRow = board[startX][startY + j], symbolColumn = board[startX + j][startY];
				for(int i = 0; i < 3; i++) 
				{
					rowFull &= board[startX + i][startY + j] == symbolRow;
					columnFull &= board[startX + j][startY + i] == symbolColumn;
					boardFull &= board[startX + i][startY + j] != null;
				}
				if(rowFull && symbolRow != null) bigBoard[startX / 3][startY / 3] = symbolRow;
				if(columnFull && symbolColumn != null) bigBoard[startX / 3][startY / 3] = symbolColumn;
			}
			//Check diagonals
			boolean diagonal1Full = true;
			boolean diagonal2Full = true;
			CellState symbol1 = board[startX][startY], symbol2 = board[startX + 2][startY];
			for(int i = 0; i < 3; i++)
			{
				diagonal1Full &= board[startX + i][startY + i] == symbol1;
				diagonal2Full &= board[startX + (2 - i)][startY + i] == symbol2;
			}
			if(diagonal1Full && symbol1 != null) bigBoard[startX / 3][startY / 3] = symbol1;
			if(diagonal2Full && symbol2 != null) bigBoard[startX / 3][startY / 3] = symbol2;

			if(boardFull && bigBoard[startX / 3][startY / 3] == null) bigBoard[startX / 3][startY / 3] = CellState.TIE;
		}
	}
	
	public CellState isWon()
	{
		boolean boardFull = true;
		//Check rows and columns
		for(int j = 0; j < 3; j++)
		{
			boolean rowFull = true;
			boolean columnFull = true;
			CellState symbolRow = bigBoard[0][j], symbolColumn = bigBoard[j][0];
			for(int i = 0; i < 3; i++) 
			{
				rowFull &= bigBoard[i][j] == symbolRow;
				columnFull &= bigBoard[j][i] == symbolColumn;
				boardFull &= bigBoard[i][j] != null;
			}
			if(rowFull && symbolRow != null) return symbolRow;
			if(columnFull && symbolColumn != null) return symbolColumn;
		}
		//Check diagonals
		boolean diagonal1Full = true;
		boolean diagonal2Full = true;
		CellState symbol1 = bigBoard[0][0], symbol2 = bigBoard[0 + 2][0];
		for(int i = 0; i < 3; i++)
		{
			diagonal1Full &= bigBoard[i][i] == symbol1;
			diagonal2Full &= bigBoard[2 - i][i] == symbol2;
		}
		if(diagonal1Full && symbol1 != null) return symbol1;
		if(diagonal2Full && symbol2 != null) return symbol2;

		if(boardFull) return CellState.TIE;
		return null;
	}
	
	private void calculateNextActiveField(int lastX, int lastY)
	{
		int x = lastX % 3, y = lastY % 3;
		if(bigBoard[x][y] != null) activeX = activeY = 3;
		else
		{
			activeX = x;
			activeY = y;
		}
	}
	
	private ErrorCode checkCoordinates(int x, int y)
	{
		System.out.println("Try to set at " + x + ", " + y);
		if(x < 0 || x > 9 || y < 0 || y > 9) return OUT_OF_BOUNDS;
		if(board[x][y] != null) return CELL_OCCUPIED;
		if(bigBoard[x / 3][y / 3] != null) return FIELD_DECIDED;
		if(activeX == 3 && activeY == 3) return NO_ERROR;
		int xMin = activeX * 3, xMax = activeX * 3 + 2;
		int yMin = activeY * 3, yMax = activeY * 3 + 2;
		if(xMin <= x && x <= xMax && yMin <= y && y <= yMax) return NO_ERROR;
		else return FIELD_INACTIVE;
	}
	
	public GameState getGameStateObject()
	{
		CellState[][] boardCopy = new CellState[9][9];
		CellState[][] bigBoardCopy = new CellState[3][3];
		for(int i = 0; i < 9; i++) for(int j = 0; j < 9; j++) boardCopy[i][j] = board[i][j];
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) bigBoardCopy[i][j] = bigBoard[i][j];
		return new GameState(boardCopy, bigBoardCopy, activeX, activeY);
	}
	
	public byte[] getBoardState()
	{
		byte[] ret = new byte[81];
		for(int i = 0; i < 81; i++) ret[i] = getByte(board[i % 9][i / 9]);
		return ret;
	}
	
	public byte[] getBigBoardState()
	{
		byte[] ret = new byte[9];
		for(int i = 0; i < 9; i++) ret[i] = getByte(bigBoard[i % 3][i / 3]);
		return ret;
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		for(int i = 0; i < 9 * 9; i++)
		{
			CellState s = board[i % 9][i / 9];
			ret += s == null ? " " : (s == CellState.TIE ? "-" : s);
			if(i % 9 == 2 || i % 9 == 5) ret += "|";
			if(i % 9 == 8) 
			{
				ret += "\r\n";
				if(i / 9 == 2 || i / 9 == 5) ret += "---+---+---\r\n";
			}
		}
		ret += "\r\n";
		for(int i = 0; i < 9; i++)
		{
			CellState s = bigBoard[i % 3][i / 3];
			ret += s == null ? " " : s;
			if(i % 3 != 2) ret += "|";
			else if(i != 8)
			{
				ret += "\r\n";
				ret += "-+-+-\r\n";
			}
		}
		ret += "\r\n";
		return ret;
	}
	
	public static enum CellState
	{
		X, O, TIE;
	}
	
	public byte getByte(CellState s)
	{
		if(s == null) return 0;
		if(s == CellState.X) return 1;
		if(s == CellState.O) return 2;
		if(s == CellState.TIE) return 3;
		return 0;
	}
}
