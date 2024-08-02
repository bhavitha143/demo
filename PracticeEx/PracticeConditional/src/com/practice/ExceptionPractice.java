package com.practice;

import java.util.Scanner;

class Eligibilityexc extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Eligibilityexception(String str) {
	
	//super(str);
		return str;
		
	}
}
public class ExceptionPractice {

		
	
	public static void main(String arg[])throws  Eligibilityexc {
		
			Scanner sc = new Scanner(System.in);
			int age = sc.nextInt();//variable
			//System.out.println("Succesful");
			try {
			
			if(age>18) {
				//try {
				throw new Eligibilityexc();
				
			}
			else {System.out.println("elgible");
			}
		}
		catch( Eligibilityexc e) {
			e.printStackTrace();
		}
		
			
			
			
		
	}
	}






