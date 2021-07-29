package main.java.view;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextField userNameField;
	JTextField passwordField;

	JLabel titleLabel;
	JLabel userNameLabel;
	JLabel passwordLabel;
	
	JButton loginButton;
	GridBagLayout gridbag = new GridBagLayout();
    
	
	public LoginPanel(String tLabel, String f1Label, String f2Label, String bLabel) {
		super();
		userNameField = new JTextField();
		passwordField = new JTextField();

		titleLabel = new JLabel(tLabel);
		userNameLabel = new JLabel(f1Label);
		passwordLabel = new JLabel(f2Label);
		
		loginButton = new JButton(bLabel);
		int buttonLength = bLabel.length();
		loginButton.setPreferredSize(new Dimension(buttonLength*15, 20));

		setUpPanel();
	}
	
	private void setUpPanel() {
		setLayout(gridbag);
		setBorder(BorderFactory.createLineBorder(Color.RED));
		passwordField.setPreferredSize(new Dimension(200,25));
		passwordField.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 1, Color.GRAY));
		userNameField.setPreferredSize(new Dimension(200,25));
		userNameField.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 1, Color.GRAY));
		GridBagConstraints constraints;
		
		// row 1
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(15,0,10,0);
		constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridbag.setConstraints(titleLabel, constraints);
		add(titleLabel);
		
		// row 2
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(5,10,5,5);
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(userNameLabel, constraints);
		add(userNameLabel);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(5,0,5,10);
		constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridbag.setConstraints(userNameField, constraints);
		add(userNameField);

		// row 3
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(5,10,5,5);
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(passwordLabel, constraints);
		add(passwordLabel);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(5,0,5,10);
		constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridbag.setConstraints(passwordField, constraints);
		add(passwordField);
		
		// row 4
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(5,5,10,10);
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
		add(loginButton, constraints);
	}

	/**
	 * @return the userNameField
	 */
	public JTextField getUserNameField() {
		return userNameField;
	}

	/**
	 * @return the passwordField
	 */
	public JTextField getPasswordField() {
		return passwordField;
	}

	/**
	 * @return the loginButton
	 */
	public JButton getLoginButton() {
		return loginButton;
	}
}
