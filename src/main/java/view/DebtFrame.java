package main.java.view;

import javax.swing.*;
import java.awt.*;

public class DebtFrame extends JFrame {

	JPanel myMainPanel;
	
	public DebtFrame() {
		
	}
	
	public DebtFrame(JPanel mainPanel) {
		super();
		myMainPanel = mainPanel;
		add(myMainPanel);
	}
	
	/**
	 * Initializes the JFrame
	 */
	public void init() {

		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		setPreferredSize(new Dimension(screenSize.width, screenSize.height));
		//setResizable(false);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
