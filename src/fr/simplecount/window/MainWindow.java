package fr.simplecount.window;

import fr.simplecount.exceptions.AllocationException;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.udojava.evalex.Expression;

public class MainWindow {
	
	private	Boolean		_visible;
	private String		_frameName;
	private	Dimension	_dimensions;
	
	private JFrame		_mainFrame;
	
	private	JPanel		_mainPanel;
	private JPanel		_buttonsPanel;
	private JPanel		_operatorsPanel;
	private JPanel		_commandPanel;
	private JPanel		_scientificPanel;
	
	private	JLabel		_resultLabel;
	
	private String		_last;
	
	
	public Boolean get_visible() {
		return _visible;
	}

	public void set_visible(Boolean _visible) {
		this._visible = _visible;
	}

	public String get_frameName() {
		return _frameName;
	}

	public void set_frameName(String _frameName) {
		this._frameName = _frameName;
	}

	public Dimension get_dimensions() {
		return _dimensions;
	}

	public void set_dimensions(Dimension _dimensions) {
		this._dimensions = _dimensions;
	}

	public JFrame get_mainFrame() {
		return _mainFrame;
	}

	public void set_mainFrame(JFrame _mainFrame) {
		this._mainFrame = _mainFrame;
	}

	public JPanel get_mainPanel() {
		return _mainPanel;
	}

	public void set_mainPanel(JPanel _mainPanel) {
		this._mainPanel = _mainPanel;
	}

	public JPanel get_buttonsPanel() {
		return _buttonsPanel;
	}

	public void set_buttonsPanel(JPanel _buttonsPanel) {
		this._buttonsPanel = _buttonsPanel;
	}

	public JPanel get_operatorsPanel() {
		return _operatorsPanel;
	}

	public void set_operatorsPanel(JPanel _operatorsPanel) {
		this._operatorsPanel = _operatorsPanel;
	}

	public JPanel get_commandPanel() {
		return _commandPanel;
	}

	public void set_commandPanel(JPanel _commandPanel) {
		this._commandPanel = _commandPanel;
	}

	public JLabel get_resultLabel() {
		return _resultLabel;
	}

	public void set_resultLabel(JLabel _resultLabel) {
		this._resultLabel = _resultLabel;
	}

	private JButton addButton(String value, JPanel comp) throws AllocationException {
		JButton button = new JButton(value);
		
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setActionCommand(value);
		button.setSize(new Dimension(100, 40));
		button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent ae)
		    {
		        JButton but = (JButton) ae.getSource();
		        
		        _last = but.getText();
		        if (_resultLabel.getText() == "0")
		        	_resultLabel.setText("");
		        _resultLabel.setText(_resultLabel.getText() + but.getText());
		        _resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		    }
		});
		comp.add(button);
        return (button);
	}
	
	public MainWindow(String name) throws AllocationException {
		if ((_dimensions = new Dimension()) == null)
			throw new AllocationException("Could not allocate Dimensions");
		_dimensions.width = 300;
		_dimensions.height = 250;
		_frameName = name;
		_visible = false;
		_mainFrame = null;
		_mainPanel = null;
	}
	
	public void		initNumbersGrid() {
		Integer tmp = new Integer(0);
		
		_buttonsPanel.setLayout(new GridLayout(4, 3));
		for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
            	tmp = ((i * 3) + j) + 1;
                try {
					addButton(tmp.toString(), _buttonsPanel);
				} catch (AllocationException e) {
					e.printStackTrace();
				}
            }
        }
		try {
			addButton("0", _buttonsPanel);
			addButton(".", _buttonsPanel);
			_buttonsPanel.add(equalButton());
		} catch (AllocationException e) {
			
		}
		
	}
	
	public JButton	equalButton() {
		JButton equal  = new JButton("=");
		ActionListener equalAction = new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				 try { 
					 System.out.println(_resultLabel.getText());
					 BigDecimal result = new Expression(_resultLabel.getText()).setPrecision(8).eval();
					 _resultLabel.setText(result.toString());
				 } catch (Exception calcEcept) {
					 try {
						 if (calcEcept.getMessage() == null)
							 new Popup("Error", "Syntax error! Check your expression :D");
						 else
							 new Popup("Error!", calcEcept.getMessage());
					} catch (AllocationException e1) {
						e1.printStackTrace();
					}
					 System.out.println(calcEcept.getMessage());
				 }
			}
			
        };
        equal.addActionListener(equalAction);
        return (equal);
	}
	
	public JButton	resetButton() {
		JButton reset  = new JButton("C");
		
		reset.setAlignmentX(Component.CENTER_ALIGNMENT);
		ActionListener resetAction = new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				 try {
					 _resultLabel.setText("0");
				 } catch (Exception calcEcept) {
					 calcEcept.printStackTrace();
				 }
			}
        };
        reset.addActionListener(resetAction);
        return (reset);
	}
	
	private JButton	backspaceButton() {
		JButton back  = new JButton("<-");
		
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		ActionListener resetAction = new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				 try {
					 if (_resultLabel.getText().length() != 0 && _resultLabel.getText() != " " && _resultLabel.getText() != "0")
					 {
						 String tmp = _resultLabel.getText().substring(0, _resultLabel.getText().length() - _last.length());
						 
						 if (tmp.length() == 0)
							 _resultLabel.setText("0");
						 else
							 _resultLabel.setText(tmp);
					 }
				 } catch (Exception calcEcept) {
					 calcEcept.printStackTrace();
				 }
			}
        };
        back.addActionListener(resetAction);
        return (back);
	}
	
	private void	initOperators() {
		_buttonsPanel.setPreferredSize(new Dimension(3 * 100, 4 * 100));
		_operatorsPanel.setPreferredSize(new Dimension(100, 4 * 50));
		_operatorsPanel.setLayout(new GridLayout(4, 2));
		/* _operatorsPanel.setLayout(new BoxLayout(_operatorsPanel, BoxLayout.Y_AXIS)); */
		
		_operatorsPanel.add(backspaceButton());
		_operatorsPanel.add(resetButton());
		try {
			addButton("+", _operatorsPanel);
			addButton("-", _operatorsPanel);
			addButton("/", _operatorsPanel);
			addButton("*", _operatorsPanel);
			addButton("%", _operatorsPanel);
			addButton("(", _operatorsPanel);
			addButton(")", _operatorsPanel);
			
			addButton("^", _scientificPanel);
			addButton("SIN(", _scientificPanel);
			addButton("COS(", _scientificPanel);
			addButton("TAN(", _scientificPanel);
			addButton("SQRT(", _scientificPanel);
			addButton("LOG(", _scientificPanel);
		} catch (AllocationException e) {
			e.printStackTrace();
		}
	}
	
	public void		init() throws AllocationException {
		if ((_mainFrame = new JFrame(_frameName)) == null)
			throw new AllocationException("Could not allocate main frame");
		if ((_mainPanel = new JPanel()) == null)
			throw new AllocationException("Could not allocate main panel");
		if ((_buttonsPanel = new JPanel()) == null)
			throw new AllocationException("Could not allocate buttons panel");
		if ((_operatorsPanel = new JPanel()) == null)
			throw new AllocationException("Could not allocate operators panel");
		if ((_commandPanel = new JPanel()) == null)
			throw new AllocationException("Could not allocate command panel");
		if ((_resultLabel = new JLabel("0")) == null)
			throw new AllocationException("Could not allocate result label");
		if ((_scientificPanel = new JPanel()) == null)
			throw new AllocationException("Could not allocate scientific label");
		
		_resultLabel.setFont(new Font(_resultLabel.getFont().getName(), Font.PLAIN, 30));
		_mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
		
		_scientificPanel.setLayout(new GridLayout(3, 2));
		
		
		initNumbersGrid();
		initOperators();

		_commandPanel.setLayout(new GridLayout(1, 3));
		_commandPanel.setPreferredSize(new Dimension(1, 300));
		_resultLabel.setPreferredSize(new Dimension(500, 50));
		_commandPanel.add(_buttonsPanel);
		_commandPanel.add(_operatorsPanel);
		_commandPanel.add(_scientificPanel);
		
		_mainPanel.add(_resultLabel);
		_mainPanel.add(_commandPanel);
		
		_mainFrame.setContentPane(_mainPanel);
		_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_mainFrame.setVisible(_visible);
		_mainFrame.pack();
	}

	public MainWindow(String name, int w, int h) throws AllocationException {
		if ((_dimensions = new Dimension()) == null)
			throw new AllocationException("Could not allocate Dimensions");
		_dimensions.width = w;
		_dimensions.height = h;
		_frameName = name;
		_visible = false;
		_mainFrame = null;
		_mainPanel = null;
	}
	
	public void	display() {
		_visible = true;
		_mainFrame.setVisible(true);
	}
	
	public void	hide() {
		_visible = false;
		_mainFrame.setVisible(_visible);
	}
	
	public Boolean	trigger() {
		_visible = (_visible) ? false : true;
		_mainFrame.setVisible(_visible);
		return (_visible);
	}
}
