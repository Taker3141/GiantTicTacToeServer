package computercamp.giantTicTacToe;

public class PlayingField
{
	private State[][] field = new State[9][9];
	
	public void setCell(int x, int y, State s)
	{
		field[x][y] = s;
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		for(int i = 0; i < 9 * 9; i++)
		{
			State s = field[i % 9][i / 9];
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
	
	public static enum State
	{
		X, O;
	}
}
