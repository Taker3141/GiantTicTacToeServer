package computercamp.giantTicTacToe.testClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

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
	
	public void waitForMessage() throws IOException
	{
		char[] buffer = new char[1204];
		in.read(buffer);
		byte[] message = new String(buffer).getBytes();
		switch(buffer[0])
		{
			case 'I': interpretInfoMessage(message); break;
			case 'W': interpretWinMessage(message); break;
			case 'E': handleError(message); break;
		}
	}
	
	private void interpretInfoMessage(byte[] message)
	{
		Main.myTurn = true; Main.initialized = true;
		if(message[2] == 0) Main.symbol = CellState.X;
		else Main.symbol = CellState.O;
		for(int i = 0; i < 9; i++)
		{
			switch(message[4 + i])
			{
				case 1: Main.bigBoard[i % 3][i / 3] = CellState.X; break;
				case 2: Main.bigBoard[i % 3][i / 3] = CellState.O; break;
				case 3: Main.bigBoard[i % 3][i / 3] = CellState.TIE; break;
				default: Main.bigBoard[i % 3][i / 3] = null; break;
			}
		}
		
		for(int i = 0; i < 81; i++)
		{
			switch(message[14 + i])
			{
				case 1: Main.board[i % 9][i / 9] = CellState.X; break;
				case 2: Main.board[i % 9][i / 9] = CellState.O; break;
				default: Main.board[i % 9][i / 9] = null; break;
			}
		}
		Main.activeX = (int)message[96]; Main.activeY = (int)message[97];
		Main.frame.repaint();
	}
	
	public void sendMoveMessage(int x, int y)
	{
		byte[] message = new byte[6];
		message[0] = 'M'; message[1] = -1;
		message[2] = (byte)x; message[3] = (byte)y;
		message[4] = '\r'; message[4] = '\n';
		out.write(new String(message).toCharArray());
		out.flush();
	}
	
	private void interpretWinMessage(byte[] message)
	{
		String text;
		if(message[2] == 3) text = "Game finished. Tie.";
		else text = "Game finished. Client " + message[2] + " won.";
		Main.done = true;
		JOptionPane.showMessageDialog(null, text, "Done", JOptionPane.OK_OPTION);
	}
	
	private void handleError(byte[] message)
	{
		//TODO exception handling
	}
}
