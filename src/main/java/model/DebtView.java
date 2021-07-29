package main.java.model;

public class DebtView {
	
	final int myMonth;
	final int myPosition;
	final String myName;
	final Double myBalance;
	final Double myPayment;
	final Double myNewBalance;
	
	public DebtView(int month,int position,String name,Double balance,Double payment,Double newBalance) {
		myMonth = month;
		myPosition = position;
		myName = name;
		myBalance = balance;
		myPayment = payment;
		myNewBalance = newBalance;
	}

	/**
	 * @return the myMonth
	 */
	public int getMyMonth() {
		return myMonth;
	}

	/**
	 * @return the myPosition
	 */
	public int getMyPosition() {
		return myPosition;
	}

	/**
	 * @return the myName
	 */
	public String getMyName() {
		return myName;
	}

	/**
	 * @return The balance before payment is made.
	 */
	public Double getMyBalance() {
		return myBalance;
	}
	
	/**
	 * @return the myPayment
	 */
	public Double getMyPayment() {
		return myPayment;
	}

	/**
	 * @return The balance after payment is made.
	 */
	public Double getMyNewBalance() {
		return myNewBalance;
	}

}
