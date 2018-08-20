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
}