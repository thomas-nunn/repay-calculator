package main.java.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Snowball implements DebtRepayment {

	private Debtor myDebtor;

	List<Debt> copyOfDebts = null;

	public static Snowball get(final Debtor theDebtor) {
		return new Snowball(theDebtor);
	}

	private Snowball(final Debtor theDebtor) {
		myDebtor = theDebtor;
	}

	
	@Override
	public Integer calculateRepayTime() {
		Integer result = 1;

		// create a copy of the original Debts
		if (!myDebtor.getDebts().isEmpty()) {
			copyOfDebts = new ArrayList<Debt>(myDebtor.getDebts());
			
			Collections.sort(copyOfDebts, new SnowballComparator());
			//Collections.sort(copyOfDebts, new HighestInterestComparator());

		}

		Double budgeted = 0.0;
		Double leftOver = 0.0;
		Double thePayment = 0.0;
		
		// algorithm
		while (!copyOfDebts.isEmpty()) {
			int size = copyOfDebts.size();
			budgeted = myDebtor.getMonthlyAmount();
			
			// add in any money left over because a Debt was paid in full
			if (leftOver > 0.0) {
				budgeted += leftOver;
				leftOver = 0.0;
			}
			System.out.println("Month: " + result + "\nBudget: " + budgeted);
			
			
			//make l payment on all Debts
			for (int i = size - 1; i >= 0; i--) {
				
				if (i == 0) {
					thePayment = budgeted;
					leftOver += copyOfDebts.get(i).makePayment(budgeted);
				} else {
					thePayment = copyOfDebts.get(i).getMonMinimum();
					leftOver += copyOfDebts.get(i).makePayment(thePayment);
					budgeted -= thePayment;
					
				}
				System.out.println(i + ". " + copyOfDebts.get(i).getName() +
						" payment: " + thePayment + " Balance: " + copyOfDebts.get(i).getBalance());
				
				// if leftOver > 0, then remove Debt because it's paid in full
				if (leftOver != 0.0 && copyOfDebts.get(i).getBalance() == 0.0) {
					System.out.println("Removed: " + copyOfDebts.get(i).getName());
					copyOfDebts.remove(i);
				}
			}
			System.out.println();
			result++;
		}

		return result-1;
	}

	@Override
	public String displayResults() {
		StringBuilder sb = new StringBuilder();
		for (Debt d : myDebtor.getDebts()) {
			sb.append(d.getName()+"\n");
		}
		return sb.toString();
	}

	@Override
	public Integer calculateSmallestRepayTime(Double balance, Double theIR, Double payment) {
		Integer months = 0;
		Double tempBalance = balance;

		while (tempBalance > 0) {
			//System.out.println(tempBalance);
			Double monthlyIR = theIR / 12;
			tempBalance += (tempBalance * monthlyIR) - payment;
			months++;
		}

		return months; 
	}
}
