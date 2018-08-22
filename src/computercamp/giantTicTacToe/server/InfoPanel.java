package computercamp.giantTicTacToe.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel
{
	private JButton buttonPrevious, buttonNext, buttonFirst, buttonLast;
	private JLabel labelMoveIndex, labelCurrentState;
	
	public InfoPanel()
	{
		buttonFirst = new JButton("<<");
		buttonFirst.setSize(50, 100);
		buttonFirst.addActionListener(new ButtonListener(-1000));
		add(buttonFirst);
		
		buttonPrevious = new JButton("<");
		buttonPrevious.setSize(50, 100);
		buttonPrevious.addActionListener(new ButtonListener(-1));
		add(buttonPrevious);
		
		labelMoveIndex = new JLabel("Move #0");
		labelMoveIndex.setSize(50, 200);
		add(labelMoveIndex);
		
		buttonNext = new JButton(">");
		buttonNext.setSize(50, 100);
		buttonNext.addActionListener(new ButtonListener(1));
		add(buttonNext);
		
		buttonLast = new JButton(">>");
		buttonLast.setSize(50, 100);
		buttonLast.addActionListener(new ButtonListener(1000));
		add(buttonLast);
		
		labelCurrentState = new JLabel("Status: Game hasn't stated yet.");
		labelCurrentState.setSize(50, 200);
		add(labelCurrentState);
	}
	
	public void update()
	{
		labelMoveIndex.setText("Move #" + Main.currentState);
		Main.frame.repaint();
	}
	
	public void setStateMessage(String message)
	{
		labelCurrentState.setText("Status: " + message);
		Main.frame.repaint();
	}
	
	private class ButtonListener implements ActionListener
	{
		private final int offset;
		
		public ButtonListener(int offset)
		{
			this.offset = offset;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int newIndex = Main.currentState + offset;
			if(offset >= 1000) newIndex = Main.gameStates.size() - 1;
			if(offset <= -1000) newIndex = 0;
			if(newIndex >= 0 && newIndex < Main.gameStates.size()) 
			{
				Main.currentState = newIndex;
				Main.gamePanel.state = Main.gameStates.get(newIndex);
				update();
			}
		}
	}
}
