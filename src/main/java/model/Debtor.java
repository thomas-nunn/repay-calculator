package main.java.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Debtor {

	/**
	 * This Debtor's name.
	 */
	private final String myName;
	
	/**
	 * The budgeted amount of money that this Debtor has to spend on
	 * Debts each month.
	 */
	private Double myMonthlyAmount;
	
	private Double myAdjustedMonthlyAmount;
	
	/**
	 * An ordered Map of this Debtor's debts.
	 */
	private List<Debt> myDebts;
	
	/**
	 * Factory method for a new Debtor without existing Debts.
	 * @param theName
	 * @param theMonAmount
	 * @return
	 */
	public static Debtor get(String theName, Double theMonAmount) {
		return new Debtor(theName, theMonAmount);
	}
	
	/**
	 * Constructor for a new Debtor without existing Debts.
	 * @param theName
	 * @param theMonAmount
	 */
	private Debtor(String theName, Double theMonAmount) {
		myName = theName;
		myMonthlyAmount = theMonAmount;
		myAdjustedMonthlyAmount = 0.0;
		myDebts = new ArrayList<Debt>();
	}
	
	/**
	 * Factory method for a new Debtor with existing Debts.
	 * @param theName
	 * @param theMonAmount
	 * @param theDebts
	 * @return
	 */
	public static Debtor get(String theName, Double theMonAmount, Set<Debt> theDebts) {
		return new Debtor(theName, theMonAmount, theDebts);
	}
	
	/**
	 * Constructor for a new Debtor with existing Debts.
	 * @param theName
	 * @param theMonAmount
	 * @param theDebts
	 */
	private Debtor(String theName, Double theMonAmount, Set<Debt> theDebts) {
		myName = theName;
		myMonthlyAmount = theMonAmount;
		myAdjustedMonthlyAmount = 0.0;
//		myDebts.addAll(theDebts);
		myDebts = new ArrayList<Debt>(theDebts);
	}
	
	/**
	 * Add a new Debt to the myDebts field.
	 * @param theDebt
	 * @return True if the Debt was added, false otherwise.
	 */
	public boolean addDebt(final Debt theDebt) {
		return myDebts.add(theDebt);
	}
	
	/**
	 * Remove a Debt from the myDebts field.
	 * @param theDebt
	 * @return
	 */
	public boolean removeDebt(final Debt theDebt) {
		return myDebts.remove(theDebt);
	}
	
	/**
	 * Add to the amount of money that this Debtor has to work with
	 * each month.
	 * @param theAmount
	 */
	public void addToMonthlyAmount(Double theAmount) {
		this.myMonthlyAmount += theAmount;
	}
	
	/**
	 * Add any unused money (when a debt's balance is less than its monthly minimum payment)
	 * to the amount of money that this Debtor has to work with each month.
	 * @param theAmount
	 */
	public void addToAdjMonthlyAmount(Double theAmount) {
		this.myAdjustedMonthlyAmount += theAmount;
	}
	
	public void clearAdjMonthlyAmount() {
		this.myAdjustedMonthlyAmount = 0.0;
	}
	
	/**
	 * @return The name of this Debtor.
	 */
	public String getName() {
		String result = myName;
		return result;
	}
	
	/**
	 * @return The monthly amount that this Debtor can spend on debts.
	 */
	public Double getMonthlyAmount() {
		Double result = myMonthlyAmount;
		return result;
	}
	
	public Double getAdjustedMonthlyAmount() {
		Double result = myAdjustedMonthlyAmount;
		return result;
	}
	
	public List<Debt> getDebts() {
		return Collections.unmodifiableList(myDebts);
	}
}
