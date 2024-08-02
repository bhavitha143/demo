package com.Assignment;

import java.util.Arrays;
import java.util.List;

public class SortInteger {

	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(2,4,0,1,56,23,4);
		
		list.stream().sorted().distinct().forEach(System.out::println);
		

	}

}
