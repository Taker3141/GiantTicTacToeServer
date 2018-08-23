package computercamp.giantTicTacToe.testClient;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;
import computercamp.giantTicTacToe.util.Renderer;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener
{
	private int Ux, Uy;
	
	public GamePanel()
	{
		addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g)
	{
		Ux = g.getClipBounds().width / 9; Uy = g.getClipBounds().height / 9;
		if(Main.state.symbol == null) Main.frame.setTitle("Ultimate Tic Tac Toe Client: Waiting for game to start");
		else if(Main.state.symbol == CellState.X) Main.frame.setTitle("Ultimate Tic Tac Toe Client: Client 1 (X)");
		else if(Main.state.symbol == CellState.O) Main.frame.setTitle("Ultimate Tic Tac Toe Client: Client 2 (O)");
		if(Main.state.initialized) Renderer.renderPlayingBoard(g, Main.state.bigBoard, Main.state.board, Main.state.activeX, Main.state.activeY, Main.state.myTurn);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int cellX = e.getX() / Ux, cellY = e.getY() / Uy;
		if(Main.state.myTurn)
		{
			Main.state.board[cellX][cellY] = Main.state.symbol;
			Main.frame.repaint();
			Main.server.sendMoveMessage(cellX, cellY);
			Main.state.myTurn = false;
		}
	}

	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}
	@Override public void mousePressed(MouseEvent arg0) {}
	@Override public void mouseReleased(MouseEvent arg0) {}
}
