package com.Assignment;

import java.util.ArrayList;

public class ListPrimenumber {

	public static void main(String[] args) {
		
		int n = 30;  
		ArrayList<Integer> list = new ArrayList<>();
		
		for(int i =2; i<=n; i++) {
			if(isPrime(i)) {
				list.add(i);
			}
		}
		System.out.println("Prime nor up to" +n+":" + list);

	}

	public static boolean isPrime(int num) {
		if(num<=1) {
			return false;	
		}
		for(int i =2; i<=Math.sqrt(num); i++) {
			if(num % i ==0) {
				return false;	
			}
		}
		return true;
	}

}
