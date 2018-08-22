package computercamp.giantTicTacToe.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import computercamp.giantTicTacToe.server.PlayingBoard.CellState;

public class Renderer
{
	private static final Color colorX = new Color(0x00, 0x80, 0x00), backgroundX = new Color(0x80, 0xFF, 0x80), 
			colorO = new Color(0x80, 0x00, 0x00), backgroundO = new Color(0xFF, 0x80, 0x80),
			backgroundTie = new Color(0x80, 0x80, 0x80);
	
	public static void renderPlayingBoard(Graphics g, CellState[][] bigBoard, CellState[][] board, int activeX, int activeY, boolean myTurn)
	{
		final int W = (int) g.getClipBounds().getWidth();
		final int H = (int) g.getClipBounds().getHeight();
		final int Ux = W / 9; final int Uy = H / 9;
		Color[][] backgroundColor = new Color[3][3];
		g.setColor(Color.WHITE);
		fillBackgroundColor(Color.WHITE, backgroundColor);
		g.fillRect(0, 0, W, H);

		if(myTurn)
		{
			g.setColor(Color.YELLOW);
			if(activeX < 3 && activeY < 3) 
			{
				g.fillRect(activeX * 3 * Ux, activeY * 3 * Uy, 3 * Ux, 3 * Uy);
				backgroundColor[activeX][activeY] = Color.YELLOW;
			}
			else 
			{
				g.fillRect(0, 0, Ux * 9, Uy * 9);
				fillBackgroundColor(Color.YELLOW, backgroundColor);
			}
		}
		
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++)
		{
			if(bigBoard[i][j] != null) 
			{
				switch(bigBoard[i][j])
				{
					case X: g.setColor(backgroundX); break;
					case O: g.setColor(backgroundO); break;
					case TIE: g.setColor(backgroundTie); break;
				}
				backgroundColor[i][j] = g.getColor();
				g.fillRect(i * 3 * Ux, j * 3 * Uy, 3 * Ux, 3 * Uy);
			}
		}
		
		for(int i = 0; i < 9; i++) for(int j = 0; j < 9; j++)
		{
			if(board[i][j] == CellState.X)
			{
				g.setColor(colorX);
				Point p1 = new Point(i * Ux + 10, j * Uy + 10);
				Point p2 = new Point((i + 1) * Ux - 10, (j + 1) * Uy - 10);
				Point p3 = new Point(i * Ux + 10, (j + 1) * Uy - 10);
				Point p4 = new Point((i + 1) * Ux - 10, j * Uy + 10);
				g.fillPolygon(new Polygon(new int[]{p1.x, p2.x, p2.x + 5, p1.x + 5}, new int[] {p1.y, p2.y, p2.y - 5, p1.y - 5}, 4));
				g.fillPolygon(new Polygon(new int[]{p3.x, p4.x, p4.x + 5, p3.x + 5}, new int[] {p3.y, p4.y, p4.y + 5, p3.y + 5}, 4));
			}
			if(board[i][j] == CellState.O)
			{
				g.setColor(colorO);
				int x = (int)(i * Ux + Ux * 0.1), y = (int)(j * Uy + Uy * 0.1);
				g.fillOval(x, y, (int)(Ux * 0.8), (int)(Uy * 0.8));
				g.setColor(backgroundColor[i / 3][j / 3]);
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
	
	private static void fillBackgroundColor(Color c, Color[][] colors)
	{
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) colors[i][j] = c;
	}
}
