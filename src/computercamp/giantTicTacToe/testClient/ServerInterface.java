package computercamp.giantTicTacToe.testClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;
import computercamp.giantTicTacToe.util.ActiveState;
import computercamp.giantTicTacToe.util.ErrorCode;

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
	
	public void waitForMessage(ActiveState state) throws IOException
	{
		char[] buffer = new char[1204];
		in.read(buffer);
		byte[] message = new String(buffer).getBytes();
		switch(buffer[0])
		{
			case 'I': interpretInfoMessage(message, state); break;
			case 'W': interpretWinMessage(message, state); break;
			case 'E': handleError(message); break;
		}
	}
	
	private void interpretInfoMessage(byte[] message, ActiveState state)
	{
		state.myTurn = true; state.initialized = true;
		if(message[2] == 0) {ActiveState.ownSymbol = CellState.X; state.symbol = CellState.X;}
		else {ActiveState.ownSymbol = CellState.O;  state.symbol = CellState.O;}
		for(int i = 0; i < 9; i++)
		{
			switch(message[4 + i])
			{
				case 1: state.bigBoard[i % 3][i / 3] = CellState.X; break;
				case 2: state.bigBoard[i % 3][i / 3] = CellState.O; break;
				case 3: state.bigBoard[i % 3][i / 3] = CellState.TIE; break;
				default: state.bigBoard[i % 3][i / 3] = null; break;
			}
		}
		
		for(int i = 0; i < 81; i++)
		{
			switch(message[14 + i])
			{
				case 1: state.board[i % 9][i / 9] = CellState.X; break;
				case 2: state.board[i % 9][i / 9] = CellState.O; break;
				default: state.board[i % 9][i / 9] = null; break;
			}
		}
		state.activeX = (int)message[96]; state.activeY = (int)message[97];
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
	
	private void interpretWinMessage(byte[] message, ActiveState state)
	{
		String text;
		if(message[2] == 3) text = "Game finished. Tie.";
		else text = "Game finished. Client " + message[2] + " won.";
		state.done = true;
		JOptionPane.showMessageDialog(null, text, "Done", JOptionPane.OK_OPTION);
	}
	
	private void handleError(byte[] message)
	{
		System.out.println("An error occurred: " + ErrorCode.values()[message[2]].toString());
	}
}
