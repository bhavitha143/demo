package com.Examples;

import java.util.Arrays;

import java.util.stream.Collectors;


public class RevesrseStringUsingStream {
	public static void main(String[] args) {

	String str = "welcom java";
	String abc = Arrays.asList(str).stream()
		    .map(s -> new StringBuilder(s).reverse().toString())
		    .collect(Collectors.toList()).get(0);
	System.out.println(abc);
}
}
