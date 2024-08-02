package com.Assignment;

public class ReverseString {

	public static void main(String[] args) {
		String str= "Capgemini";
		StringBuilder sb = new StringBuilder(str);
		sb.reverse();
		System.out.println("Reverse of string is :" +sb);

	}

}
