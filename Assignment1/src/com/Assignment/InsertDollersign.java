package com.Assignment;


import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InsertDollersign {

	public static void main(String[] args) {
		String str = "capgemini";
		String result = insertDollerAfterFourChars(str);
		System.out.println(result);

	}

	private static String insertDollerAfterFourChars(String str) {
		
		return IntStream.range(0, str.length()).mapToObj(i -> (i > 0 && i % 4 ==0)
				? "$" + str.charAt(i): String.valueOf(str.charAt(i)))
				.collect(Collectors.joining());
	}

}
