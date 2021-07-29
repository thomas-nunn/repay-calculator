package main.java.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class DebtToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.LIGHT_GRAY);


	public DebtToolBar() {
		super();
		setUpBar();
	}
	
	private void setUpBar() {
		this.setFloatable(false);

	}
	
	public void addJButton(String name,ActionListener al) {
		JButton jb = new JButton(name);
		jb.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		jb.addActionListener(al);
		jb.setPreferredSize(new Dimension(40,28));
		this.add(jb);
	}
	
	public void addLabelTextPanel(String name2,JTextField jtf,boolean editable) {
		JPanel jp = new JPanel();
		jp.setBorder(border);
		jp.setMaximumSize(new Dimension(200,30));
		//jp.setLayout(new GridLayout(1,2));
		JLabel label = new JLabel(name2);
		//label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		jtf.setPreferredSize(new Dimension(70,18));
		
		//jtf.setMaximumSize(new Dimension(70,18));
		jp.add(label);
		jp.add(jtf);
		this.add(jp);
	}
}
