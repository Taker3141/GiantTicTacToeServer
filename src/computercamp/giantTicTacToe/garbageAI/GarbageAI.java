package computercamp.giantTicTacToe.garbageAI;

import java.util.*;

import computercamp.giantTicTacToe.util.ActiveState;

public class GarbageAI
{
	private Random random = new Random();
	
	public int[] calculateBestMove(ActiveState state)
	{
		List<Move> possibleMoves = new ArrayList<Move>();
		for(int x = 0; x < 9; x++) for(int y = 0; y < 9; y++)
		{
			if(state.board[x][y] != null) continue;
			if(state.bigBoard[x / 3][y / 3] != null) continue;
			if(x < 0 || x > 9 || y < 0 || y > 9) continue;
			if(state.activeX == 3 && state.activeY == 3) 
			{
				possibleMoves.add(new Move(x, y, null));
				continue;
			}
			int xMin = state.activeX * 3, xMax = state.activeX * 3 + 2;
			int yMin = state.activeY * 3, yMax = state.activeY * 3 + 2;
			if(xMin <= x && x <= xMax && yMin <= y && y <= yMax) 
			{
				possibleMoves.add(new Move(x, y, null));
			}
		}
		int index = random.nextInt(possibleMoves.size());
		Move move = possibleMoves.get(index);

		return new int[]{move.x, move.y};
	}
}
