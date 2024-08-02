package com.Assignment;

public class PalindromeString {

	public static void main(String[] args) {
		String str = "MADAM", reverseStr = "";
	   //can read the same in reverse order showing same string
	     
		int strLength = str.length();

	    for (int i = (strLength - 1); i >=0; --i) {
	      reverseStr = reverseStr + str.charAt(i);
	    }

	    if (str.toLowerCase().equals(reverseStr.toLowerCase())) {
	      System.out.println(str + " is a Palindrome");
	    }
	    else {
	      System.out.println(str + " is not a Palindrome");
	    }
	}

}
 