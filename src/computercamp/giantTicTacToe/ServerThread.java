package computercamp.giantTicTacToe;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread
{
	public final int threadID = threadCounter++;
	private final Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public static int threadCounter = 0;
	
	public ServerThread(Socket clientSocket)
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
	}

	@Override
	public void run()
	{
		try
		{
			String inputLine = null;
			while((inputLine = in.readLine()) != null)
			{
				out.println(inputLine);
			}
		} 
		catch (Exception e)
		{
			println("Thread crashed :(");
			e.printStackTrace();
		}
	}
	
	private void println(String message)
	{
		System.out.println("Thread " + threadID + ": " + message);
	}
}
