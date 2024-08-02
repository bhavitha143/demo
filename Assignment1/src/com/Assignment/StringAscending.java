package com.Assignment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringAscending {

	public static void main(String[] args) {
		
		List<String> list = Arrays.asList("Capgemini","Accenture","TCS","EPAM");
		String str = list.stream().sorted().collect(Collectors.joining(","));
		System.out.println(str);
		


	}
	}


