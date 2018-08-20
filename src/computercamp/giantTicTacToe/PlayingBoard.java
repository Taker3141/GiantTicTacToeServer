package computercamp.giantTicTacToe;

public class PlayingBoard
{
	private CellState[][] board = new CellState[9][9];
	private CellState[][] bigBoard = new CellState[3][3];
	int activeX = 3, activeY = 3;
	
	public void setCell(int x, int y, CellState s)
	{
		if(checkCoordinates(x, y))
		{
			board[x][y] = s;
			calculateBigBoardState();
			calculateNextActiveField(x, y);
		}
		else
		{
			//TODO error handling
		}
	}
	
	private void calculateBigBoardState()
	{
		for(int startX = 0; startX < 9; startX += 3)
		{
			for(int startY = 0; startY < 9; startY += 3)
			{
				//TODO calculate if a Field is won
			}
		}
	}
	
	private void calculateNextActiveField(int lastX, int lastY)
	{
		//TODO calculate next active Field
	}
	
	private boolean checkCoordinates(int x, int y)
	{
		if(board[x][y] != null) return false;
		if(x <= 9 || y <= 9) return false;
		if(activeX == 3 && activeY == 3) return true;
		int xMin = activeX * 3, xMax = activeX + 2;
		int yMin = activeY * 3, yMax = activeY + 2;
		if(xMin <= x && x <= xMax && yMin <= y && y <= yMax) return true;
		else return false;
	}
	
	public CellState isWon()
	{
		//TODO calculate if game is won
		return null;
	}
	
	public byte[] getBoardState()
	{
		byte[] ret = new byte[81];
		for(int i = 0; i < 81; i++)
		{
			ret[i] = getByte(board[i % 9][i / 9]);
		}
		return ret;
	}
	
	public byte[] getBigBoardState()
	{
		byte[] ret = new byte[9];
		for(int i = 0; i < 9; i++)
		{
			ret[i] = getByte(bigBoard[i % 3][i / 3]);
		}
		return ret;
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		for(int i = 0; i < 9 * 9; i++)
		{
			CellState s = board[i % 9][i / 9];
			ret += s == null ? " " : s;
			if(i % 9 == 2 || i % 9 == 5) ret += "|";
			if(i % 9 == 8) 
			{
				ret += "\n";
				if(i / 9 == 2 || i / 9 == 5) ret += "---+---+---\n";
			}
		}
		return ret;
	}
	
	public static enum CellState
	{
		X, O;
	}
	
	public byte getByte(CellState s)
	{
		if(s == null) return 0;
		if(s == CellState.X) return 1;
		if(s == CellState.O) return 2;
		return 0;
	}
}
