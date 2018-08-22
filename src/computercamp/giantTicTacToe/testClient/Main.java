package computercamp.giantTicTacToe.testClient;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class Main
{
	public static ServerInterface server;
	public static CellState[][] board = new CellState[9][9];
	public static CellState[][] bigBoard = new CellState[3][3];
	public static boolean myTurn = false;
	public static int activeX = 3, activeY = 3;
	public static CellState symbol = null;
	public static boolean initialized = false;
	public static boolean done = false;
	
	public static JFrame frame;
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		System.out.println("Testing Client running");
		
		frame = new JFrame();
		frame.setSize(900, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new GamePanel());
		
		frame.setVisible(true);
			
		try
		{
			server = new ServerInterface(new Socket(InetAddress.getLocalHost(), PORT));
			
			while(true) server.waitForMessage();
		}
		catch(SocketException e1)
		{
			if(done) System.out.println("Game finished. Server closed.");
			else e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
