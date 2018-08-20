package computercamp.giantTicTacToe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import computercamp.giantTicTacToe.PlayingField.State;

public class Main
{
	private static ClientInterface[] clients = new ClientInterface[2];
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		//System.out.println(generateRandomTestingField());
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server Running");
			Socket clientSocket = serverSocket.accept();
			clients[0] = new ClientInterface(clientSocket);
			System.out.println("Client 0 accepted");
			clientSocket = serverSocket.accept();
			clients[1] = new ClientInterface(clientSocket);
			System.out.println("Client 1 accepted");
			byte[] testMessage = "Test message".getBytes();
			byte[] buffer;
			while(true)
			{
				clients[0].sendMessage(testMessage);
				buffer = clients[0].getMessage();
				System.out.println(new String(buffer));
				clients[1].sendMessage(testMessage);
				buffer = clients[1].getMessage();
				System.out.println(new String(buffer));
			}
		} 
		catch (Exception e)
		{
			System.out.println("Server Crashed");
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
