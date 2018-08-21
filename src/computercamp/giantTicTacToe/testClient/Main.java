package computercamp.giantTicTacToe.testClient;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class Main
{
	public static ServerInterface server;
	private static CellState[][] board = new CellState[9][9];
	private static CellState[][] bigBoard = new CellState[3][3];
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Testing Client running");
			server = new ServerInterface(new Socket(InetAddress.getLocalHost(), PORT));
		}
		catch(SocketException e1)
		{
			//throw e1;
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
