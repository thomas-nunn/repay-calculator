package main.java.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Repayment {

	private Debtor myDebtor;
	Comparator<Debt> repayComparator;
	List<Debt> copyOfDebts = null;
	Object[][] myData;

	/**
	 * An ArrayList of DebtView ArrayLists.
	 */
	ArrayList<ArrayList<DebtView>> paymentData;

	public static Repayment get(final Debtor theDebtor, final Comparator<Debt> comp) {
		return new Repayment(theDebtor, comp);
	}

	private Repayment(final Debtor theDebtor, final Comparator<Debt> comp) {
		myDebtor = theDebtor;
		repayComparator = comp;
		paymentData = new ArrayList<ArrayList<DebtView>>();
	}


	public Integer calculateRepayTime() {
		Integer result = 1;
		Integer entries = 0;

		// create a copy of the original Debts
		if (!myDebtor.getDebts().isEmpty()) {
			copyOfDebts = new ArrayList<Debt>(myDebtor.getDebts());
			Collections.sort(copyOfDebts, repayComparator);
		}

		Double budgeted = 0.0;
		Double leftOver = 0.0;
		Double thePayment = 0.0;
		ArrayList<DebtView> monthOfViews;


		// algorithm
		while (!copyOfDebts.isEmpty()) {
			int size = copyOfDebts.size();
			budgeted = myDebtor.getMonthlyAmount();

			// add in any money left over because a Debt was paid in full
			if (leftOver > 0.0) {
				budgeted += leftOver;
				leftOver = 0.0;
			}
			monthOfViews = new ArrayList<DebtView>();

			//make l payment on all Debts
			for (int i = size-1; i >= 0; i--) {
				Double currentBalance = copyOfDebts.get(i).getBalance();
				if (i == 0) {
					thePayment = budgeted;
					leftOver += copyOfDebts.get(i).makePayment(budgeted);
				} else {
					thePayment = copyOfDebts.get(i).getMonMinimum();
					leftOver += copyOfDebts.get(i).makePayment(thePayment);
					budgeted -= thePayment;

				}
				monthOfViews.add(new DebtView(result, i+1, copyOfDebts.get(i).getName(),currentBalance, thePayment, copyOfDebts.get(i).getBalance()));
				entries++;

				// if leftOver > 0, then remove Debt because it's paid in full
				if (leftOver != 0.0 && copyOfDebts.get(i).getBalance() == 0.0) {
					copyOfDebts.remove(i);
				}

			}
			//			System.out.println();
			result++;
			paymentData.add(monthOfViews);
		}

		buildObjArray(entries);
		return result-1;
	}

	/**
	 * Builds the 2d Object array myData for use in JTables.
	 * 
	 * @param numEntries
	 */
	private void buildObjArray(int numEntries) {
		myData = new Object[numEntries + paymentData.size() - 1][6];

		Integer row = 0;
		for (int i = 0; i < paymentData.size(); i++) {
			for (int j = 0; j < paymentData.get(i).size(); j++) {

				for (int k = 0; k < 6; k++) {

					if (k == 0) {
						myData[row][k] = paymentData.get(i).get(j).getMyMonth();
					} else if (k == 1) {
						myData[row][k] = paymentData.get(i).get(j).getMyPosition();
					} else if (k == 2) {
						myData[row][k] = paymentData.get(i).get(j).getMyName();
					} else if (k == 3) {
						long balance = Math.round(paymentData.get(i).get(j).getMyBalance() * 100) / 100;
						myData[row][k] = balance;
					} else if (k == 4) {
						long payment = Math.round(paymentData.get(i).get(j).getMyPayment() * 100) / 100;
						myData[row][k] = payment;
					} else {
						long balance = Math.round(paymentData.get(i).get(j).getMyNewBalance() * 100) / 100;
						myData[row][k] = balance;
					}
				}
				row++;
			}
			row++;
		}
	}

	public Object[][] getData() {
		return myData;
	}

}
