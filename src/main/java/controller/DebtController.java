package main.java.controller;

//Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

import main.java.model.*;
import main.java.resources.DatabaseValues;
import main.java.view.DebtDataPanel;
import main.java.view.DebtToolBar;
import main.java.view.ExpensePanel;
import main.java.view.LoginPanel;
import org.apache.derby.iapi.util.StringUtil;
import org.apache.derby.jdbc.EmbeddedDriver;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.java.resources.Utils;

public class DebtController {

	JFrame mainFrame = new JFrame();
	final static Integer INSETS = 5;
	String user = "Local User";
	int SCREENWIDTH = 0;
	int SCREENHEIGHT = 0;
	int OutputTableWidth = 0;
	int OutputTableHeight = 0;
	JPanel loginContainer;
	JButton runButton;
	JTable expenseTable;
	JTabbedPane tabPane;
	Border border;
	JPanel mainPanel;
	JPanel resultsPanel = new JPanel();
	JTextField netIncomeField;
	ExpensePanel exPanel;
	JDialog popUp;

	GridBagLayout gbl = new GridBagLayout();
	DatabaseMetaData dbmeta;
	int tabPaneWidth;
	DebtToolBar toolBar = new DebtToolBar();

	/**
	 * The Set of tables in the database.
	 */
	Set<String> dbTables = new HashSet<String>();

	JScrollPane outputPane2;
	JTable outputTable2;
	String[] outputColumnNames2 = {"Month","Position","Debt Name","Balance","Payment Made","New Balance"};

	Connection dbConnection = null;

	JRadioButton snowball = new JRadioButton("Snowball");
	JRadioButton highestInterest = new JRadioButton("Highest Interest");
	int resultsCount = 1;

	public DebtController() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		SCREENWIDTH = screenSize.width;
		SCREENHEIGHT = screenSize.height;
		tabPane = new JTabbedPane();
		exPanel = new ExpensePanel(user, false);
		setUpJFrame();
		setUpButtons();
		setDBSystemDir();
	}

	private void setDBSystemDir() {

		String userHomeDir = System.getProperty("user.home", ".");
		String systemDir = userHomeDir + "/.repayDB";

		// Set the db system directory.
		System.setProperty("derby.system.home", systemDir);

		// create the database if it doesn't already exist


		try {
			dbConnection = DriverManager.getConnection(DatabaseValues.DERBY_REPAY_DB_CREATE_TRUE);
			if (dbConnection != null) {
				dbTables = getDBTables(dbConnection);
				createDbTables(dbConnection.createStatement());
				exPanel.retrieveUserData(dbConnection, user);
				dbConnection.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	private boolean createDbTables(final Statement theStatement) {
		boolean result = true;

		Map<String,String> tableCreateMap = Map.ofEntries(
				Map.entry("debts", DatabaseValues.getDebtsTableCreateString),
				Map.entry("debt", DatabaseValues.getGetDebtTableCreateString),
				Map.entry("bills", DatabaseValues.getGetBillsTableCreateString),
				Map.entry("bill", DatabaseValues.getGetGetBillTableCreateString));

		for (String tableName : tableCreateMap.keySet()) {
			if (!dbTables.contains(tableName)) {
				try {
					theStatement.execute(tableCreateMap.get(tableName));
				} catch (SQLException throwables) {
					throwables.printStackTrace();
					result = false;
				}
				//statement.execute("INSERT INTO debts values ('car', 425.00, 3.9, 19845.33, 'Thomas Nunn')");
			}
		}
		return result;
	}
	private void setUpJFrame() {

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.getHSBColor(.25f, 0.9f, 0.7f));
		//mainPanel.setBackground(Color.GRAY);
		border = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.LIGHT_GRAY);
		exPanel.setBorder(border);
		resultsPanel.setBorder(border);
		
		tabPaneWidth = mainPanel.getWidth()-(exPanel.getWidth()+20);
		int tabPaneHeight = (exPanel.getHeight());
		resultsPanel.setPreferredSize(new Dimension(tabPaneWidth, tabPaneHeight));
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		
		JScrollPane mainScrollPane = new JScrollPane(mainPanel);
		mainFrame.add(mainScrollPane);

		mainPanel.setLayout(gbl);
		GridBagConstraints constraints;
		createWelcomePane();

		// row 1
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		//constraints.insets = new Insets(0,0,INSETS,0);
		constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
		gbl.setConstraints(toolBar, constraints);
		mainPanel.add(toolBar);

		// row 2
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(INSETS,INSETS,INSETS,INSETS);
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		gbl.setConstraints(exPanel, constraints);
		mainPanel.add(exPanel);

		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(INSETS,0,INSETS,INSETS);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(resultsPanel, constraints);
		mainPanel.add(resultsPanel);
		resultsPanel.add(tabPane);
		
		mainFrame.setPreferredSize(new Dimension(Math.min(SCREENWIDTH, 1366), Math.min(SCREENHEIGHT, 728)));
		//setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void createWelcomePane() {
		JPanel welcomePanel = new JPanel();
		JTextPane jtp = new JTextPane();
		jtp.setText("Welcome to the Debt Repayment Application");
		welcomePanel.add(jtp);
		tabPane.add("Welcome", welcomePanel);
	}

	/**
	 * Get all the tables in a database.
	 * 
	 * @param targetDBConn The db connection.
	 * @return A Set of Strings representing each table in the db.
	 * @throws SQLException
	 */
	private Set<String> getDBTables(Connection targetDBConn) throws SQLException {
		Set<String> set = new HashSet<String>();
		dbmeta = targetDBConn.getMetaData();
		readDBTable(set, dbmeta, "TABLE", null);
		readDBTable(set, dbmeta, "VIEW", null);
		return set;
	}

	private void readDBTable(Set<String> set, DatabaseMetaData dbmeta, String searchCriteria, String schema)
			throws SQLException {
		ResultSet rs = dbmeta.getTables(null, schema, null, new String[]{ searchCriteria });
		while (rs.next()) {
			set.add(rs.getString("TABLE_NAME").toLowerCase());
		}
	}

	private void setUpButtons() {

		final ButtonGroup jbg = new ButtonGroup();
		jbg.add(snowball);
		jbg.add(highestInterest);
		snowball.setSelected(true);

		netIncomeField = new JTextField();
		toolBar.addJButton("File", null);
		toolBar.add(Box.createHorizontalStrut(5));
		toolBar.addJButton("Edit", null);
		exPanel.getSaveButton().setBorder(BorderFactory.createRaisedSoftBevelBorder());
		toolBar.add(Box.createHorizontalStrut(5));
		toolBar.add(exPanel.getSaveButton());
		toolBar.add(Box.createHorizontalStrut(5));
		toolBar.addJButton("Help", null);
		toolBar.add(Box.createHorizontalStrut(5));
		toolBar.addLabelTextPanel("Monthly Net Income", netIncomeField, true);
		toolBar.add(Box.createHorizontalStrut(5));
		toolBar.add(snowball);
		toolBar.add(highestInterest);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.addJButton("Run", new RunListener());
		toolBar.add(Box.createVerticalStrut(10));
	}

	/**
	 * Extracts the Debt data from expenseTable and generates a panel
	 * for displaying the Debt Repayment results.
	 */
	private void buildResultsPanel() {

		
		expenseTable = exPanel.getInputTable();
		// for cases where user didn't hit enter on the last edited cell
		if (expenseTable.isEditing()) {
			expenseTable.getCellEditor().stopCellEditing();
		}

		Set<Debt> theDebts = new HashSet<Debt>();
		Double totalMonthlyPayments = 0.0; // total all expenses
		Double totalMonthlyDebt = 0.0;
		Double totalMonthlyNonDebt = 0.0; //total non-debt

		for (int i = 0; i < expenseTable.getRowCount(); i++) {
			String name = "";
			Double apr = 0.0;
			Double min = 0.0;
			Double bal = 0.0;
			Boolean include = false;
			Boolean isDebt = false;

			for (int j = 1; j < expenseTable.getColumnCount(); j++) {
				Object obj = expenseTable.getValueAt(i, j);

				// fix this to allow for user rearranging columns?
				if (j == 1) {
					name = (String) obj;
					if (name.equals("")) {
						break;
					}
				} else if (j == 2) {
					min = (Double) obj;
				} else if (j == 3) {
					isDebt = (Boolean) obj;
				} else if (j == 4) {
					apr = (Double) obj;
				} else if (j == 5) {
					bal = (Double) obj;
				} else if (j == 6) {
					include = (Boolean) obj;
				}
			}

			// add up the total of all monthly payments
			totalMonthlyPayments += min;

			// create Debt and add to theDebts if the row is filled in
			if (!name.equals("") && isDebt && include) {
				theDebts.add(Debt.get(name, apr, min, bal));
				totalMonthlyDebt += min;
			} else {
				totalMonthlyNonDebt += min;
			}
		}

		if (Utils.isNumeric(netIncomeField.getText())) {

			Double monthlyNetIncome = Double.parseDouble(netIncomeField.getText());
			Double monthlyRepaymentBudget = monthlyNetIncome - totalMonthlyNonDebt;
			Debtor debtor = Debtor.get(exPanel.getName(), monthlyRepaymentBudget, theDebts);
			Repayment repayment;
			String repayTypeLabel;

			if (snowball.isSelected()) {
				repayment = Repayment.get(debtor, new SnowballComparator());
				repayTypeLabel = "Snowball";
			} else {
				repayment = Repayment.get(debtor, new HighestInterestComparator());
				repayTypeLabel = "Highest Interest First";
			}

			final String[] myLabels = {"Months to Repay Debts", "Debt Repayment Type", "Monthly Net Income", "Total Monthly Expenses",
					"Total Monthly Debts", "Debt Repayment Monthly Budget"};
			final String[] myValues = {repayment.calculateRepayTime().toString(), repayTypeLabel, monthlyNetIncome.toString(), totalMonthlyPayments.toString()
					, totalMonthlyDebt.toString(), monthlyRepaymentBudget.toString()};

			final DebtDataPanel ddp = new DebtDataPanel(3, 2, 700);
			final JLabel[] labels = ddp.getMyLabels();
			final JLabel[] values = ddp.getMyValues();

			for (int i = 0; i < labels.length; i++) {
				labels[i].setText(myLabels[i]);
				values[i].setText(myValues[i]);
				values[i].setForeground(Color.ORANGE);

			}

			DefaultTableModel inputModel = new DefaultTableModel(repayment.getData(), outputColumnNames2) {
				private static final long serialVersionUID = 2L;

				final Class<?>[] types = new Class[]{String.class,
						String.class, String.class,
						Double.class, Double.class,
						Double.class};

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			// set up results JTable
			outputTable2 = new JTable(inputModel);

			outputTable2.getColumnModel().getColumn(0).setPreferredWidth(20);
			outputTable2.getColumnModel().getColumn(1).setPreferredWidth(55);
			outputTable2.getColumnModel().getColumn(2).setPreferredWidth(125);
			outputTable2.getColumnModel().getColumn(3).setPreferredWidth(125);
			outputTable2.getColumnModel().getColumn(3).setCellRenderer(NumberRenderer.getCurrencyRenderer());
			outputTable2.getColumnModel().getColumn(4).setPreferredWidth(125);
			outputTable2.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getCurrencyRenderer());
			outputTable2.getColumnModel().getColumn(5).setPreferredWidth(125);
			outputTable2.getColumnModel().getColumn(5).setCellRenderer(NumberRenderer.getCurrencyRenderer());

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			outputTable2.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			outputTable2.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

			outputPane2 = new JScrollPane(outputTable2);
			outputTable2.setFillsViewportHeight(true);

			JPanel tabPanel = new JPanel();
			tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
			tabPanel.add(ddp);
			tabPanel.add(outputPane2);

			tabPane.addTab("Monthly Results " + resultsCount, tabPanel);
			tabPane.setToolTipTextAt(resultsCount, "Displays the Debt Repayment month by month");
			resultsCount++;
		} else {
			JOptionPane.showMessageDialog(mainFrame, "Please enter a valid monthly net income.");
		}
	}

	/////////////////////////////////Listener Classes Below/////////////////////////////////////////

	/**
	 * A Listener class for the run button.
	 * 
	 * @author Thomas
	 *
	 */
	private class RunListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			buildResultsPanel();
		}
	}



}
