package computercamp.giantTicTacToe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import computercamp.giantTicTacToe.PlayingField.State;

public class Main
{
	private static ServerThread[] threads = new ServerThread[2];
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		//System.out.println(generateRandomTestingField());
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server Running");
			while(threads[0] == null || threads[1] == null)
			{
				Socket clientSocket = serverSocket.accept();
				ServerThread thread = new ServerThread(clientSocket);
				threads[ServerThread.threadCounter - 1] = thread;
				thread.start();
			}
			while(true) Thread.sleep(1000);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {try{serverSocket.close();} catch(Exception e) {};}
	}
	
	private static PlayingField generateRandomTestingField()
	{
		PlayingField field = new PlayingField();
		Random rand = new Random();
		
		for(int i = 0; i < 9; i++) for(int j = 0; j < 9; j++) switch(rand.nextInt() % 3)
		{
			case 0: field.setCell(i, j, null); break;
			case 1: field.setCell(i, j, State.X); break;
			case 2: field.setCell(i, j, State.O); break;
		}
		
		return field;
	}
}
