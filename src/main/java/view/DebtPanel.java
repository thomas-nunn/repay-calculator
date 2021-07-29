package main.java.view;

import main.java.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * !!!!!!!!!  Unused !!!!!!!!!!!
 * @author Thomas
 *
 */
public class DebtPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JScrollPane inputPane;
	JTable inputTable;

	JScrollPane outputPane2;
	JTable outputTable2;
	String[] outputColumnNames2 = {"Month","Position","Debt Name","Payment Made","Balance"};

	DefaultTableModel inputModel;

	JButton runButton;
	JTabbedPane tabPane;
	TextPanel namePanel = new TextPanel("Name", true);
	TextPanel budgetPanel = new TextPanel("Monthly Budget", true);
	TextPanel totalMonthsPanel = new TextPanel("Months to Repay Debts", false);

	/**
	 * An ArrayList of DebtView ArrayLists.
	 */
	ArrayList<ArrayList<DebtView>> paymentData;


	public DebtPanel() {

		tabPane = new JTabbedPane();
		paymentData = new ArrayList<ArrayList<DebtView>>();
		runButton = new JButton("Run");
		setUpInputTable();
		buildPanel();
		setUpButtons();
	}

	private void setUpInputTable() {

		inputModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2L;
			Class<?>[] types = new Class [] {  
					String.class, String.class, Double.class,
					Double.class, Double.class
			};  
			@Override  
			public Class<?> getColumnClass(int columnIndex) {  
				return types [columnIndex];  
			}  
		};

		inputTable = new JTable(inputModel);
		inputPane = new JScrollPane(inputTable);
		inputModel.addColumn("");
		inputModel.addColumn("Debt Name");
		inputModel.addColumn("APR");
		inputModel.addColumn("Minimum Payment");
		inputModel.addColumn("Balance");
		inputTable.getColumnModel().getColumn(0).setPreferredWidth(10);

		for (int i = 1; i <= 20; i++) inputModel.addRow(new Object[] {i,"",0,0,0});
	}

	private void setUpButtons() {

		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// for cases where user didn't hit enter on the last edited cell
				if (inputTable.isEditing()) {
					inputTable.getCellEditor().stopCellEditing();
				}

				Set<Debt> theDebts = new HashSet<Debt>();
				for (int i = 0; i < inputTable.getRowCount(); i++) {
					String name = "";
					Double apr = 0.0;
					Double min = 0.0;
					Double bal = 0.0;

					for (int j = 1; j < inputTable.getColumnCount(); j++) {
						Object obj = inputTable.getValueAt(i, j);

						// fix this to allow for user rearranging columns?
						if (j == 1) {
							name = (String) obj;
							if (name.equals("")) {
								break;
							}
						} else if (j == 2) {
							apr = (Double) obj;
						} else if (j == 3) {
							min = (Double) obj;
						} else if (j == 4) {
							bal = (Double) obj;
						}
					}
					
					// create Debt and add to theDebts if the row is filled in
					if (!name.equals("")) {
						Debt debt = Debt.get(name, apr, min, bal);
						theDebts.add(debt);
					}
				}

				Debtor debtor = Debtor.get(namePanel.getTextField().getText(),
						Double.parseDouble(budgetPanel.getTextField().getText()), theDebts);
				Repayment repayment = Repayment.get(debtor, new SnowballComparator());
				totalMonthsPanel.getTextField().setText(repayment.calculateRepayTime().toString());
				outputTable2 = new JTable(repayment.getData(), outputColumnNames2);
				outputPane2 = new JScrollPane(outputTable2);
				outputTable2.setFillsViewportHeight(true);
				tabPane.add("Monthly Results", outputPane2);
			}
		});
	}

	/**
	 * 
	 */
	private void buildPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
		userPanel.add(namePanel);
		userPanel.add(budgetPanel);
		userPanel.add(inputPane);
		userPanel.add(runButton);

		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.add(totalMonthsPanel);
		resultPanel.add(new TextPanel("Debt-Free Date", false));
		resultPanel.add(tabPane);

		inputTable.setFillsViewportHeight(true);

		this.add(userPanel);
		this.add(resultPanel);
		resultPanel.setPreferredSize(userPanel.getPreferredSize());
	}

	/**
	 * A Class for creating text fields with labels.
	 * 
	 * @author Thomas
	 *
	 */
	private class TextPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean isEditable;
		JTextField myTF;
		JLabel myLabel;

		private TextPanel(String theFieldLabel, boolean editable) {
			super();
			isEditable = editable;
			myTF = new JTextField();
			myLabel = new JLabel(theFieldLabel);
			buildPanel();
		}

		private void buildPanel() {
			this.setLayout(new GridLayout(1,2));
			if (!isEditable) {
				myTF.setEditable(false);
			}
			myLabel.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(myLabel);
			this.add(myTF);
		}

		public JTextField getTextField() {
			return myTF;
		}
	}

}
