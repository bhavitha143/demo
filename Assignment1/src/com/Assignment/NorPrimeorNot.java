package com.Assignment;

public class NorPrimeorNot {

	public static void main(String[] args) {
		int a = 34;
		boolean flag = true;
		for(int i=2; i<= a/2; ++i) {
			if(a%i ==0);
			flag = true;
			break;
		}
		if (!flag)
		System.out.println(a + "is prime nor");
		else {System.out.println(a + " is not a prime number");

	}

}
}
