package computercamp.giantTicTacToe.garbageAI;

import java.util.*;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;
import computercamp.giantTicTacToe.util.ActiveState;

public class GarbageAI
{
	private Random random = new Random();
	private CellState symbol;
	
	public int[] calculateBestMove(ActiveState state)
	{
		symbol = state.symbol;
		Move move = getMoveRecursively(state, 5, 5);

		return new int[]{move.x, move.y};
	}
	
	private Move getMoveRecursively(ActiveState state, int depth, int maxDepth)
	{
		List<Move> possibleMoves = state.getPossibleMoves();
		if(depth == maxDepth)
		{
			RecursionThread[] threads = new RecursionThread[2];
			threads[0] = new RecursionThread(0, possibleMoves.size() / 2, maxDepth - 1, possibleMoves, state);
			threads[1] = new RecursionThread(threads[0].endIndex + 1, possibleMoves.size() - 1, maxDepth, possibleMoves, state);
			for(RecursionThread t : threads) t.start();
			try {for(RecursionThread t : threads) t.join();} catch(Exception e) {e.printStackTrace();}
		}
		else for(Move m : possibleMoves)
		{
			ActiveState newState = state.applyMove(m);
			CellState winner = state.isWon();
			if(winner != null) m.value = (winner == CellState.X ? 1000 : (winner == CellState.O ? -1000 : 0));
			else
			{
				if(depth == 0) m.value = newState.getValue();
				else m.value = getMoveRecursively(newState, depth - 1, maxDepth).value;
			}
		}
		Collections.sort(possibleMoves);
		if(depth % 2 == (symbol == CellState.O ? 0 : 1)) Collections.reverse(possibleMoves);
		int i = 0;
		if(maxDepth == depth) for(i = 0; i < possibleMoves.size(); i++)
		{
			if(possibleMoves.get(i) != possibleMoves.get(0)) break;
			System.out.println(possibleMoves.get(0).value + " - " + possibleMoves.get(possibleMoves.size() - 1).value);
		}
		if(possibleMoves.size() == 1) return possibleMoves.get(0);
		if(possibleMoves.size() == 1) {Move retMove = new Move(-1, -1, CellState.TIE); retMove.value = -1000; return new Move(-1, -1, CellState.TIE);}
		return possibleMoves.get(random.nextInt(i + 1));
	}
	
	private class RecursionThread extends Thread
	{
		public final int startIndex, endIndex;
		public final List<Move> list;
		public final ActiveState state;
		public final int depth;
		
		public RecursionThread(int startIndex, int endIndex, int depth, List<Move> list, ActiveState state)
		{
			this.startIndex = startIndex; this.endIndex = endIndex;
			this.list = list;
			this.state = state;
			this.depth = depth;
		}
		
		@Override
		public void run()
		{
			for(int i = startIndex; i <= endIndex; i++)
			{
				Move m = list.get(i);
				ActiveState newState = state.applyMove(m);
				if(depth == 0) m.value = newState.getValue();
				else m.value = getMoveRecursively(newState, depth - 1, depth).value;
				if(m.value > 10) break;
			}
		}
	}
}
