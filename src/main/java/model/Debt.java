package main.java.model;

public class Debt {

	/**
	 * The name of this Debt.
	 */
	private String myName;

	/**
	 * The balance when this Debt was created.
	 */
	private Double myStartingBalance;

	/**
	 * The balance remaining on this Debt.
	 */
	private Double myBalance;

	/**
	 * The interest rate of this Debt.
	 */
	private Double myInterestRate;

	/**
	 * The monthly minimum payment for this Debt.
	 */
	private Double myMonthlyMinimum;


	public static Debt get(String theName, Double theIR, Double theMM, Double theBalance) {

		return new Debt(theName, theIR, theMM, theBalance);
	}

	private Debt(String theName, Double theIR, Double theMM, Double theBalance) {
		super();
		myName = theName;
		myInterestRate = theIR * .01;
		myMonthlyMinimum = theMM;
		myStartingBalance = theBalance;
		myBalance = theBalance;
	}

	public Integer getMMRepayTime() {
		Integer months = 0;
		Double tempBalance = this.myBalance;
		while (tempBalance > 0) {
			tempBalance += tempBalance * this.myInterestRate;
			tempBalance -= this.myMonthlyMinimum;
			months++;
		}

		return months; 
	}

	public String getName() {
		final String tempName = myName;
		return tempName;
	}

	public Double getIR() {
		final Double result = myInterestRate;
		return result;
	}

	public Double getBalance() {
		final Double result = myBalance;
		return result;
	}

	public Double getStartingBalance() {
		final Double result = myStartingBalance;
		return result;
	}

	public Double getMonMinimum() {
		final Double result = myMonthlyMinimum;
		return result;
	}

	//	/**
	//	 * Reduce this Debt's balance by the monthly minimum payment.
	//	 * @return The leftover amount if balance is paid.
	//	 */
	//	protected Double makePayment() {
	//		Double result = 0.0;
	//		this.myBalance -= this.myMonthlyMinimum;
	//		if (this.myBalance < 0) {
	//			result = Math.abs(this.myBalance);
	//		} else {
	//			this.myBalance += this.myBalance * (this.myInterestRate / 12);
	//		}
	//		return result;
	//	}

	/**
	 * Reduce this Debt's balance by the payment amount or the monthly minimum
	 * if the payment is 0. After payment is made the interest is calculated
	 * and added to the balance.
	 * 
	 * @param payment The payment amount.
	 * @return The leftover amount if balance is paid.
	 */
	protected Double makePayment(final Double payment) {
		Double result = 0.0;

		// make payment
		if (payment == 0.0) {
			this.myBalance -= this.myMonthlyMinimum;
		} else {
			this.myBalance -= payment;
		}
		
		// add interest if applicable
		if (this.myBalance < 0) {
			result = Math.abs(this.myBalance);
			this.myBalance = 0.0;
		} else {
			this.myBalance += this.myBalance * (this.myInterestRate / 12);
		}

		return result;
	}

	/**
	 * @Override
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Debt: " + myName);
		sb.append("\nBalance: " + myBalance);
		sb.append("\nRate: " + myInterestRate);
		sb.append("\nMonthly Minimum: " + myMonthlyMinimum);

		return sb.toString();
	}


	/**
	 * Returns True if two Debts have the same name and balance, false otherwise.
	 */
	@Override public boolean equals(Object o) {
		Debt other;
		if (o instanceof Debt) {
			other = (Debt)o;
		} else {
			return false;
		}
		return this.myName.equals(other.myName) && (this.myBalance == other.myBalance);
	}


}
