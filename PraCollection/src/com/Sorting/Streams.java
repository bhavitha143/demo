package com.Sorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Streams {
	public static void main(String args[]) {
		
		List<Integer> list = Arrays.asList(2,2,5,6,3,10,8,17,8);
		Stream<Integer> data = list.stream();  // create stream
		
		
		//remove duplicate values
		list.stream().distinct().forEach(System.out::println);
		 // just using stream()
		list.stream().forEach(n -> System.out.println(n)); 
		
		//double the values
		list.stream().map(n -> n*2).forEach(n-> System.out.println(n));
		
		// filter the values
		list.stream().filter(n -> n%2 == 0).sorted().map(n -> n*2).forEach(n-> System.out.println(n)); 
		
		// ref lowest value // remove duplicate values distinct()
		// 2,3....17
		int ref = list.stream().sorted().distinct().skip(1).findFirst().get();
		 System.out.println(ref);
		 
		 //ref1 highest
		 // 17,10...2
		 int ref1 = list.stream().sorted(Collections.reverseOrder()).distinct().skip(1).findFirst().get();
		 System.out.println(ref1); 
	
	}

}
