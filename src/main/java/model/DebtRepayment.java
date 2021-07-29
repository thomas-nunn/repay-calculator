package main.java.model;

public interface DebtRepayment {
	
	/**
	 * Calculates the total time in months to repay the smallest Debt.
	 * @return The time in months.
	 */
	public Integer calculateSmallestRepayTime(Double balance, Double theIR, Double payment);
	
	/**
	 * Calculates the total time in months to repay all Debts.
	 * @return The time in months.
	 */
	public Integer calculateRepayTime();
	
	/**
	 * A String representation of the repayment statistics.
	 * @return The String results.
	 */
	public String displayResults();

}
