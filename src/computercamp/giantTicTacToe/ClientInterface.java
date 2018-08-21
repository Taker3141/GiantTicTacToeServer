package computercamp.giantTicTacToe;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import computercamp.giantTicTacToe.PlayingBoard.CellState;

public class ClientInterface
{
	public final int clientID = clientCounter++;
	public final CellState symbol;
	private final Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public static int clientCounter = 0;
	public static final byte SEPARATOR = -1;
	
	public ClientInterface(Socket clientSocket)
	{
		socket = clientSocket;
		try
		{
			println("New connection on port " + socket.getPort() + " to " + clientSocket.getInetAddress());
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(clientID == 0) symbol = CellState.X;
		else symbol = CellState.O;
	}

	public byte[] getMessage() throws SocketException, IOException
	{
		char[] buffer = new char[1204];
		in.read(buffer);
		return new String(buffer).getBytes();
	}
	
	public void sendMessage(byte[] message)
	{
		out.print(new String(message));
		out.flush();
	}
	
	private void println(String message)
	{
		System.out.println("Thread " + clientID + ": " + message);
	}
	
	public static byte[] composeInfoMessage(PlayingBoard board, ClientInterface client)
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
	
	public static void interpretMoveMessage(byte[] message, PlayingBoard board, ClientInterface client)
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
	
	public static byte[] composeWinMessage(ClientInterface winner)
	{
		byte[] message = new byte[4];
		int i = 0;
		message[i++] = 'W'; message[i++] = SEPARATOR;
		message[i++] = (byte)winner.clientID; message[i++] = SEPARATOR;
		return message;
	}
	
	public static byte[] copyBytesToLocation(byte[] dest, byte[] src, int loc)
	{
		byte[] ret = dest;
		for(int i = 0; i < src.length; i++) ret[loc + i] = src[i];
		return ret;
	}
}