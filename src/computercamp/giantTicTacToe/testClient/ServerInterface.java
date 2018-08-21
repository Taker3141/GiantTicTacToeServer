package computercamp.giantTicTacToe.testClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerInterface
{
	public Socket socket = null;
	public PrintWriter out;
	public BufferedReader in;
	
	public ServerInterface(Socket socket)
	{
		this.socket = socket;
		
		try
		{
			System.out.println("Connected to server");
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void waitForInfoMessage() throws IOException
	{
		char[] buffer = new char[1204];
		in.read(buffer);
		//TODO interpret message
	}
}
