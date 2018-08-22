package computercamp.giantTicTacToe.server;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

import javax.swing.JFrame;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class Main
{
	private static ClientInterface[] clients = new ClientInterface[2];
	
	public static PlayingBoard board = new PlayingBoard();
	public static CellState winner = null;
	public static JFrame frame;
	public static ServerGamePanel gamePanel;
	public static InfoPanel infoPanel;
	public static List<GameState> gameStates = new ArrayList<GameState>();
	public static int currentState = 0;
	public static File logFile = new File("log.txt");
	public static PrintWriter logger;
	
	public static final int PORT = 3141;
	
	public static void main(String[] args)
	{
		frame = new JFrame("Ultimate Tic Tac Toe Server");
		frame.setSize(900, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		gamePanel = new ServerGamePanel();
		frame.add(gamePanel);
		infoPanel = new InfoPanel();
		frame.add(infoPanel);
		frame.addComponentListener(new ComponentListener()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				gamePanel.setLocation(0, 0);
				gamePanel.setSize(frame.getWidth(), frame.getHeight() - 100);
				infoPanel.setLocation(0, frame.getHeight() - 100);
				infoPanel.setSize(frame.getWidth(), 100);
			}
			
			@Override public void componentHidden(ComponentEvent arg0) {}
			@Override public void componentMoved(ComponentEvent arg0) {}
			@Override public void componentShown(ComponentEvent arg0) {}
			
		});
		frame.setVisible(true);
		
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
			logger = new PrintWriter(new FileOutputStream(logFile));
			updateGameDisplay();
			while(true)
			{
				while(!moveRoutine(clients[0]));
				if(winner != null) break;
				while(!moveRoutine(clients[1]));
				if(winner != null) break;
			}
			byte[] winMessage = ClientInterface.composeWinMessage(winner);
			String messageToDisplay = "";
			if(winner == CellState.TIE) messageToDisplay = "Game finished. It's a tie.";
			else if(winner == CellState.X) messageToDisplay = "Game finished. Client 1 (X) won.";
			else if(winner == CellState.O) messageToDisplay = "Game finished. Client 2 (O) won.";
			clients[0].sendMessage(winMessage);
			clients[1].sendMessage(winMessage);
			infoPanel.setStateMessage(messageToDisplay);
			logger.println(messageToDisplay);
			logger.close();
			System.out.println(messageToDisplay);
			System.out.println("Sending log to clients.");
			{
				FileInputStream fileInput = new FileInputStream(logFile);
				byte[] fileBuffer = new byte[fileInput.available()];
				fileInput.read(fileBuffer);
				fileInput.close();
				clients[0].sendMessage(fileBuffer);
				clients[1].sendMessage(fileBuffer);
			}
			System.out.println("Closing server.");
		} 
		catch (Exception e)
		{
			System.out.println("Server Crashed");
			e.printStackTrace();
		}
		finally {try{logger.flush(); logger.close(); serverSocket.close();} catch(Exception e) {};}
	}
	
	private static boolean moveRoutine(ClientInterface client) throws SocketException
	{
		boolean ret = false;
		try
		{
			byte[] message;
			byte[] buffer;
			System.out.println("It's Client " + (client.clientID + 1) + "'s turn");
			infoPanel.setStateMessage("It's Client " + (client.clientID + 1) + "'s turn");
			message = client.composeInfoMessage(board);
			client.sendMessage(message);
			buffer = client.getMessage();
			ret = client.interpretMoveMessage(buffer, board);
			if(ret) updateGameDisplay();
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
	
	private static void updateGameDisplay()
	{
		if(currentState == gameStates.size() - 1) currentState++;
		gameStates.add(board.getGameStateObject());
		gamePanel.state = gameStates.get(currentState);
		logger.println("Move #" + (gameStates.size() - 1) + ":");
		logger.print(board);
		logger.println();
		infoPanel.update();
		frame.repaint();
	}
}
