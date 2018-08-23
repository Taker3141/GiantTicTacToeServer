package computercamp.giantTicTacToe.util;

import java.util.ArrayList;
import java.util.List;

import computercamp.giantTicTacToe.garbageAI.Move;
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
	public static CellState ownSymbol = CellState.X;
	
	public ActiveState deepCopy()
	{
		ActiveState ret = new ActiveState();
		for(int i = 0; i < 9; i++) for(int j = 0; j < 9; j++) ret.board[i][j] = board[i][j];
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) ret.bigBoard[i][j] = bigBoard[i][j];
		ret.myTurn = myTurn;
		ret.activeX = activeX; ret.activeY = activeY;
		ret.symbol = symbol;
		ret.initialized = initialized;
		ret.done = done;
		return ret;
	}
	
	public List<Move> getPossibleMoves()
	{
		List<Move> possibleMoves = new ArrayList<Move>();
		for(int x = 0; x < 9; x++) for(int y = 0; y < 9; y++)
		{
			if(board[x][y] != null) continue;
			if(bigBoard[x / 3][y / 3] != null) continue;
			if(x < 0 || x > 9 || y < 0 || y > 9) continue;
			if(activeX == 3 && activeY == 3) 
			{
				possibleMoves.add(new Move(x, y, symbol));
				continue;
			}
			int xMin = activeX * 3, xMax = activeX * 3 + 2;
			int yMin = activeY * 3, yMax = activeY * 3 + 2;
			if(xMin <= x && x <= xMax && yMin <= y && y <= yMax) 
			{
				possibleMoves.add(new Move(x, y, symbol));
			}
		}
		return possibleMoves;
	}
	
	public ActiveState applyMove(Move move)
	{
		ActiveState ret = deepCopy();
		ret.board[move.x][move.y] = move.symbol; 
		ret.activeX = move.x % 3; ret.activeY = move.y % 3;
		ret.calculateBigBoardState();
		ret.symbol = (ret.symbol == CellState.X) ? ret.symbol = CellState.O : CellState.X;
		if(ret.bigBoard[ret.activeX][ret.activeY] != null) {ret.activeX = 3; ret.activeY = 3;}
		return ret;
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
	
	public int getValue()
	{
		int value = 0;
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) 
		{
			if(bigBoard[i][j] != null) switch(bigBoard[i][j])
			{
				case X: value++; break;
				case O: value--; break;
				default:
			}
		}
		CellState winner = isWon();
		if(winner != null) return 100 * (winner == ownSymbol ? -1 : 1);
		return value * (symbol == ownSymbol ? -1 : 1);
	}
}
