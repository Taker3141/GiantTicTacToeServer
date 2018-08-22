package computercamp.giantTicTacToe.testClient;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

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
		if(Main.symbol == null) Main.frame.setTitle("Ultimate Tic Tac Toe Client: Waiting for game to start");
		//TODO
		if(Main.initialized) Renderer.renderPlayingBoard(g, Main.bigBoard, Main.board, Main.activeX, Main.activeY, Main.myTurn);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int cellX = e.getX() / Ux, cellY = e.getY() / Uy;
		if(Main.myTurn)
		{
			Main.board[cellX][cellY] = Main.symbol;
			Main.frame.repaint();
			Main.server.sendMoveMessage(cellX, cellY);
			Main.myTurn = false;
		}
	}

	@Override public void mouseEntered(MouseEvent arg0) {}
	@Override public void mouseExited(MouseEvent arg0) {}
	@Override public void mousePressed(MouseEvent arg0) {}
	@Override public void mouseReleased(MouseEvent arg0) {}
}
