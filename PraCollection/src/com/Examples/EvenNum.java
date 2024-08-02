package com.Examples;

import java.util.Scanner;

public class EvenNum {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);// create scanr class obj
		System.out.println("Enter a num:");
		int n = sc.nextInt(); // read the value from user
		//method call
		findevenodd(n);
		}
	public static void findevenodd(int n) { //user defined method not retun so void
	
		if(n%2 == 0)  // method body // check even odd 
		System.out.println("even"); 
	else
		System.out.println("odd");
	}
		
	}