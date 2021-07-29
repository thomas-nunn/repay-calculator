package main.java.model;

import java.util.Comparator;

public class SnowballComparator implements Comparator<Debt> {


	@Override
	public int compare(Debt o1, Debt o2) {
		int result = 0;
		if (o1.getBalance() < o2.getBalance()) {
			result = -1;
		} else if (o1.getBalance() > o2.getBalance()) {
			result = 1;
		}
		return result;
	}

}
