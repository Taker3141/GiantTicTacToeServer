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
		for(Move m : possibleMoves)
		{
			ActiveState newState = state.applyMove(m);
			if(depth == 0) m.value = newState.getValue();
			else m.value = getMoveRecursively(newState, depth - 1, maxDepth).value;
			if(m.value > 10) break;
		}
		if(depth % 2 == (symbol == CellState.X ? 0 : 1)) Collections.sort(possibleMoves);
		else Collections.sort(possibleMoves, Collections.reverseOrder());
		int i = 0;
		if(maxDepth == depth) for(i = 0; i < possibleMoves.size(); i++)
		{
			if(possibleMoves.get(i) != possibleMoves.get(0)) break;
		}
		if(possibleMoves.size() == 1) return possibleMoves.get(0);
		if(possibleMoves.size() == 1) {Move retMove = new Move(-1, -1, CellState.TIE); retMove.value = -1000; return new Move(-1, -1, CellState.TIE);}
		return possibleMoves.get(random.nextInt(i + 1));
	}
}
