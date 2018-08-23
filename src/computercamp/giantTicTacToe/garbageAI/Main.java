package computercamp.giantTicTacToe.garbageAI;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import computercamp.giantTicTacToe.testClient.ServerInterface;
import computercamp.giantTicTacToe.util.ActiveState;

public class Main
{
	public static ServerInterface server;
	public static GarbageAI ai;
	public static ActiveState state = new ActiveState();
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		System.out.println("Garbage AI (GAI) running");
		
		try
		{
			server = new ServerInterface(new Socket(InetAddress.getLocalHost(), PORT));
			ai = new GarbageAI();
			
			while(true) 
			{
				server.waitForMessage(state);
				int[] move = ai.calculateBestMove(state);
				server.sendMoveMessage(move[0], move[1]);
			}
		}
		catch(SocketException e1)
		{
			if(state.done) System.out.println("Game finished. Server closed.");
			else e1.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
