package computercamp.giantTicTacToe;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import computercamp.giantTicTacToe.PlayingBoard.CellState;

public class Main
{
	private static ClientInterface[] clients = new ClientInterface[2];
	
	public static PlayingBoard board;
	
	public static final int PORT = 3141;
	public static final byte SEPARATOR = -1;
	
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
			byte[] message;
			byte[] buffer;
			while(true)
			{
				message = composeInfoMessage(clients[0]);
				clients[0].sendMessage(message);
				buffer = clients[0].getMessage();
				interpretMoveMessage(buffer, clients[0]);
				
				message = composeInfoMessage(clients[1]);
				clients[1].sendMessage(message);
				buffer = clients[1].getMessage();
				interpretMoveMessage(buffer, clients[1]);
			}
		} 
		catch (Exception e)
		{
			System.out.println("Server Crashed");
			e.printStackTrace();
		}
		finally {try{serverSocket.close();} catch(Exception e) {};}
	}
	
	private static byte[] composeInfoMessage(ClientInterface client)
	{
		byte[] message = new byte[99];
		int i = 0;
		message[i++] = 'I'; message[i++] = SEPARATOR;
		message[i++] = (byte)client.clientID; message[i++] = SEPARATOR;
		
		message = copyBytesToLocation(message, board.getBigBoardState(), i); i += 9; message[i++] = SEPARATOR;
		message = copyBytesToLocation(message, board.getBoardState(), i); i += 81; message[i++] = SEPARATOR;
		message[i++] = (byte)board.activeX; message[i++] = (byte)board.activeY; message[i++] = SEPARATOR;
		return message;
	}
	
	private static void interpretMoveMessage(byte[] message, ClientInterface client)
	{
		int i = 0;
		if(message[i++] != 'M') 
		{
			System.out.println("This is not a move message!");
			return;
		}
		i++;
		int x = message[i++], y = message[i++];
		board.setCell(x, y, client.symbol);
	}
	
	private static byte[] composeWinMessage(ClientInterface winner)
	{
		byte[] message = new byte[4];
		int i = 0;
		message[i++] = 'W'; message[i++] = SEPARATOR;
		message[i++] = (byte)winner.clientID; message[i++] = SEPARATOR;
		return message;
	}
	
	private static byte[] copyBytesToLocation(byte[] dest, byte[] src, int loc)
	{
		byte[] ret = dest;
		for(int i = 0; i < src.length; i++) ret[loc + i] = src[i];
		return ret;
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
