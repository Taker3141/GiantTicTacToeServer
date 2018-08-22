package computercamp.giantTicTacToe.server;

import java.net.*;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class Main
{
	private static ClientInterface[] clients = new ClientInterface[2];
	
	public static PlayingBoard board = new PlayingBoard();
	public static CellState winner = null;
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
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
				while(!moveRoutine(clients[0]));
				if(winner != null) break;
				while(!moveRoutine(clients[1]));
				if(winner != null) break;
			}
			clients[0].sendMessage(ClientInterface.composeWinMessage(winner));
		} 
		catch (Exception e)
		{
			System.out.println("Server Crashed");
			e.printStackTrace();
		}
		finally {try{serverSocket.close();} catch(Exception e) {};}
	}
	
	private static boolean moveRoutine(ClientInterface client) throws SocketException
	{
		boolean ret = false;
		try
		{
			byte[] message;
			byte[] buffer;
			System.out.println("It's Client " + client.clientID + "'s turn");
			message = client.composeInfoMessage(board);
			client.sendMessage(message);
			buffer = client.getMessage();
			ret = client.interpretMoveMessage(buffer, board);
			System.out.println("Client " + client.clientID + " made a move");
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
		return ret;
	}
}
