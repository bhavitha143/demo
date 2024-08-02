package com.Examples;

public class AdditionTwo {
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		int a=10;
		int b= 20;
		int c= add(a,b);// return call a method
		int d= sub(a,b);
		System.out.println("sum of two num =" +c);
		System.out.println("subtraction of two num =" +d);
}
	
	private static int sub(int a, int b) {
		int g;
		g=a-b;
		return g;
	}

	public static int add(int n1,int n2) {  // user defined method
		int s;
		
		s=n1+n2;  
		return s; 
		
	}

}
