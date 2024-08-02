package com.practice;

public class ExceptionDemo {
	static void validate(int age) {
		if (age<18) {
		System.out.println( "not elgible");
	}
	else {
		System.out.println("elgible");
	}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		validate(17);
		System.out.println("Succesful");
	}

}
