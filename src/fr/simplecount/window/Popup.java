package fr.simplecount.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.simplecount.exceptions.AllocationException;

public class Popup {

	private JFrame	_mainFrame;
	
	private JPanel	_mainPanel;
	
	private JLabel	_message;
	
	private JButton	_okButton;
	
	public Popup(String title, String message) throws AllocationException {
		if ((_mainFrame = new JFrame(title)) == null)
			throw new AllocationException("Could not allocate memory for popup main frame");
		if ((_mainPanel = new JPanel()) == null)
			throw new AllocationException("Could not allocate memory for popup main panel");
		if ((_message = new JLabel(message)) == null)
			throw new AllocationException("Could not allocate memory for popup label");
		if ((_okButton = new JButton("Ok")) == null)
			throw new AllocationException("Could not allocate memory for popup label");
		
		
		_okButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent ae)
		    {
		        _mainFrame.dispose();
		    }
		});
		
		_mainPanel.add(_message);
		_mainPanel.add(_okButton);
		
		_mainFrame.setContentPane(_mainPanel);
		_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_mainFrame.setVisible(true);
		_mainFrame.setSize(300, 150);
		_mainFrame.pack();
	}
}
