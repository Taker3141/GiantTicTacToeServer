package computercamp.giantTicTacToe.testClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener
{
	private int W;
	private int H;
	private int Ux, Uy;
	
	public GamePanel()
	{
		addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g)
	{
		W = (int) g.getClipBounds().getWidth();
		H = (int) g.getClipBounds().getHeight();
		Ux = W / 9; Uy = H / 9;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, W, H);
		
		for(int i = 0; i < 9; i++) for(int j = 0; j < 9; j++)
		{
			if(Main.board[i][j] == CellState.X)
			{
				g.setColor(new Color(0x00, 0x80, 0x00));
				Point p1 = new Point(i * Ux + 10, j * Uy + 10);
				Point p2 = new Point((i + 1) * Ux - 10, (j + 1) * Uy - 10);
				Point p3 = new Point(i * Ux + 10, (j + 1) * Uy - 10);
				Point p4 = new Point((i + 1) * Ux - 10, j * Uy + 10);
				g.fillPolygon(new Polygon(new int[]{p1.x, p2.x, p2.x + 5, p1.x + 5}, new int[] {p1.y, p2.y, p2.y - 5, p1.y - 5}, 4));
				g.fillPolygon(new Polygon(new int[]{p3.x, p4.x, p4.x + 5, p3.x + 5}, new int[] {p3.y, p4.y, p4.y + 5, p3.y + 5}, 4));
			}
			if(Main.board[i][j] == CellState.O)
			{
				g.setColor(new Color(0x80, 0x00, 0x00));
				int x = (int)(i * Ux + Ux * 0.1), y = (int)(j * Uy + Uy * 0.1);
				g.fillOval(x, y, (int)(Ux * 0.8), (int)(Uy * 0.8));
				g.setColor(Color.WHITE);
				x += Ux * 0.1; y += Uy * 0.1;
				g.fillOval(x, y, (int)(Ux * 0.6), (int)(Uy * 0.6));
			}
		}
		
		g.setColor(Color.BLACK);
		for(int i = 1; i < 9; i++)
		{
			int x = i * Ux, y = i * Uy;
			g.drawLine(0, y, W, y);
			g.drawLine(x, 0, x, H);
			if(i % 3 == 0)
			{
				g.drawLine(0, 1 + y, W, 1 + y);
				g.drawLine(1 + x, 0, 1 + x, H);
				g.drawLine(0, -1 + y, W, -1 + y);
				g.drawLine(-1 + x, 0, -1 + x, H);
			}
		}
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
