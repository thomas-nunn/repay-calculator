package main.java.view;

import main.java.model.NumberRenderer;
import main.java.resources.DatabaseValues;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ExpensePanel extends JScrollPane {

	/**
	 * The number of rows for the main JTable.
	 */
	private static final int NUMROWS = 60;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Set<String> debtnames = new HashSet<String>();

	/**
	 * The name of this user.
	 */
	String userName;
	Boolean hasData;
	DefaultTableModel inputModel;
	JTable inputTable;
	JLabel nameLabel;
	JButton saveButton;
	JButton editButton;

	GridBagLayout gridbag = new GridBagLayout();

	/**
	 *  @param user The user's name.
	 * @param hasData True if user has existing expenses.
	 */
	public ExpensePanel(final String user, final boolean hasData) {
		userName = user;
		this.hasData = hasData;
		nameLabel = new JLabel(userName);
		saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveButtonListener());
		editButton = new JButton("Edit");
		setUpInputTable();
	}

	/**
	 * Creates an empty JTable with column names.
	 */
	private void setUpInputTable() {

		inputModel = getTableModel();
		inputTable = new JTable(inputModel);
		
		inputTable.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == 4) {
					int row = e.getFirstRow();
					int column = e.getColumn();
					
				}
			}
		});

		this.setViewportView(inputTable);
		inputModel.addColumn("");
		inputModel.addColumn("Expense Name");
		inputModel.addColumn("Monthly Payment");
		inputModel.addColumn("Debt?");
		inputModel.addColumn("APR");
		inputModel.addColumn("Balance");
		inputModel.addColumn("Include?");
		inputTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		inputTable.getColumnModel().getColumn(1).setPreferredWidth(125);
		inputTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		inputTable.getColumnModel().getColumn(5).setPreferredWidth(125);

		inputTable.getColumnModel().getColumn(2).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		//inputTable.getColumnModel().getColumn(4).setCellRenderer(NumberRenderer.getPercentRenderer());

		inputTable.getColumnModel().getColumn(5).setCellRenderer(NumberRenderer.getCurrencyRenderer());

		for (int i = 1; i <= NUMROWS; i++) {
			inputModel.addRow(new Object[] { i, "", 0.0, false, 0.0, 0.0, false });
		}

		if (hasData) {
			// populate table with data
			// retrieveUserData();
		}

		inputTable.setPreferredScrollableViewportSize(new Dimension(600, 620));
	}

	private DefaultTableModel getTableModel() {

		DefaultTableModel result = new DefaultTableModel() {

			private static final long serialVersionUID = 2L;

			Class<?>[] types = new Class[] { String.class,
					String.class, Double.class,
					Boolean.class, Double.class,
					Double.class, Boolean.class };

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				boolean result = false;
				if (column == 0) {
					return result;
				}
				if (column == 1 || column == 2 || column == 3) {
					result = true;
				} else {
					if ((boolean) inputTable.getValueAt(row, 3)) {
						result = true;
					}
				}
				return result;
			}
		};
		return result;
	}

	/**
	 * Save user's unique expenses to the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
	private boolean saveExpenses() throws SQLException {
		boolean result = false;
		Boolean isDebt = false;
		try {

			Connection dbConnection = DriverManager.getConnection(DatabaseValues.DERBY_REPAY_DB_CREATE_FALSE);

			// for cases where user didn't hit enter on the last edited cell
			if (inputTable.isEditing()) {
				inputTable.getCellEditor().stopCellEditing();
			}

			for (int i = 0; i < inputTable.getRowCount(); i++) {
				String expenseName = "";
				Double payment = 0.0;
				Double apr = 0.0;
				Double bal = 0.0;

				for (int j = 1; j < inputTable.getColumnCount(); j++) {
					Object obj = inputTable.getValueAt(i, j);

					// fix this to allow for user rearranging columns?
					if (j == 1) {
						expenseName = (String) obj;
						if (expenseName.equals("")) {
							break;
						}
					} else if (j == 2) {
						payment = (Double) obj;
					} else if (j == 3) {
						isDebt = (Boolean) obj;
					} else if (j == 4) {
						apr = (Double) obj;
					} else if (j == 5) {
						bal = (Double) obj;
					}
				}

				// check for empty rows and existing expenses
				if (!expenseName.equals("") && debtnames.add(expenseName)) {

					if (isDebt) {
						String debtInsert = "INSERT INTO debt VALUES (?,?,?,?,?)";
						PreparedStatement updateDebts = dbConnection
								.prepareStatement(debtInsert);

						updateDebts.setString(1, expenseName);
						updateDebts.setDouble(2, payment);
						updateDebts.setDouble(3, apr);
						updateDebts.setDouble(4, bal);
						updateDebts.setString(5, userName);
						updateDebts.executeUpdate();
						updateDebts.close();

					} else {
						String billInsert = "INSERT INTO bill VALUES (?,?,?)";
						PreparedStatement updateBills = dbConnection
								.prepareStatement(billInsert);

						updateBills.setString(1, expenseName);
						updateBills.setDouble(2, payment);
						updateBills.setString(3, userName);
						updateBills.executeUpdate();
						updateBills.close();
					}
				}
			}

			//DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}


		return result;
	}

	public boolean retrieveUserData(Connection conn, String user) {
		boolean result = false;
		Statement stmt;
		ResultSet rs;
		try {
			int i = 0;
			stmt = conn.createStatement();
			String query = "SELECT * FROM bill WHERE username =\'" + user
					+ "\'";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String name = rs.getString(1);
				debtnames.add(name);
				inputModel.setValueAt(name, i, 1);
				inputModel.setValueAt((Double) rs.getDouble(2), i, 2);
				i++;

			}

			String query2 = "SELECT * FROM debt WHERE username =\'" + user
					+ "\'";
			rs = stmt.executeQuery(query2);
			while (rs.next()) {
				String name = rs.getString(1);
				debtnames.add(name);
				inputModel.setValueAt(name, i, 1);
				inputModel.setValueAt((Double) rs.getDouble(2), i, 2);
				inputModel.setValueAt(true, i, 3);
				Double rate = (Double) rs.getDouble(3);
				System.out.println(rate);
				inputModel.setValueAt(rate, i, 4);
				inputModel.setValueAt((Double) rs.getDouble(4), i, 5);
				inputModel.setValueAt(true, i, 6);
				i++;
			}

			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @return the inputTable
	 */
	public JTable getInputTable() {
		return inputTable;
	}

	/**
	 *
	 * @return
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 *
	 */
	private class SaveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				saveExpenses();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
