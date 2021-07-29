package main.java.model;

import java.util.Comparator;

public class HighestInterestComparator implements Comparator<Debt> {

	@Override
	public int compare(Debt o1, Debt o2) {
		int result = 0;
		if (o1.getIR() > o2.getIR()) {
			result = -1;
		} else if (o1.getIR() < o2.getIR()) {
			result = 1;
		}
		return result;
	}

}
