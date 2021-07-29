package main.java.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

public class DebtDataPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Border border;
	JLabel[] myLabels;
	JLabel[] myValues;

	public DebtDataPanel(int rows, int columns, int maxWidth) {
		super(new GridLayout(rows,columns));
		myLabels = new JLabel[rows*columns];
		myValues = new JLabel[rows*columns];
		border = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.LIGHT_GRAY);
		buildPanel(rows,columns,maxWidth);
	}

	private void buildPanel(int rows, int columns, int panelMaxWidth) {
		for (int i = 0; i < rows*columns; i++) {
				JPanel labelsPanel = new JPanel(new BorderLayout());
				JLabel nameLabel = new JLabel();
				JLabel valueLabel = new JLabel();

				valueLabel.setBackground(Color.BLUE);
				Font valueFont = new Font("Serif",Font.BOLD,14);
				valueLabel.setFont(valueFont);

				myLabels[i] = nameLabel;
				myValues[i] = valueLabel;
				labelsPanel.add(nameLabel,BorderLayout.WEST);
				labelsPanel.add(valueLabel,BorderLayout.EAST);

				labelsPanel.setBorder(border);
				this.add(labelsPanel);
				this.setMaximumSize(new Dimension(panelMaxWidth,70));
		}
	}
	
	/**
	 * @return the myLabels
	 */
	public JLabel[] getMyLabels() {
		return myLabels;
	}

	/**
	 * @return the myValues
	 */
	public JLabel[] getMyValues() {
		return myValues;
	}
}
