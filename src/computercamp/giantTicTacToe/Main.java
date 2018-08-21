package computercamp.giantTicTacToe;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import computercamp.giantTicTacToe.PlayingBoard.CellState;

public class Main
{
	private static ClientInterface[] clients = new ClientInterface[2];
	
	public static PlayingBoard board;
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		board = generateRandomTestingField();
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
			while(true)
			{
				moveRoutine(clients[0]);
				moveRoutine(clients[1]);
			}
		} 
		catch (Exception e)
		{
			System.out.println("Server Crashed");
			e.printStackTrace();
		}
		finally {try{serverSocket.close();} catch(Exception e) {};}
	}
	
	private static void moveRoutine(ClientInterface client) throws SocketException
	{
		try
		{
			byte[] message;
			byte[] buffer;
			message = client.composeInfoMessage(board);
			client.sendMessage(message);
			buffer = client.getMessage();
			client.interpretMoveMessage(buffer, board);
		}
		catch(SocketException e)
		{
			System.out.println("Client " + client.clientID + " disconnected.");
			throw e;
		}
		catch(Exception e)
		{
			System.out.println("An Exception occurred O_o");
			e.printStackTrace();
		}
	}
	
	private static PlayingBoard generateRandomTestingField()
	{
		PlayingBoard field = new PlayingBoard();
		Random rand = new Random();
		
		for(int i = 0; i < 9; i++) for(int j = 0; j < 9; j++) switch(rand.nextInt() % 3)
		{
			case 0: field.setCell(i, j, null); break;
			case 1: field.setCell(i, j, CellState.X); break;
			case 2: field.setCell(i, j, CellState.O); break;
		}
		
		return field;
	}
}
