package com.Assignment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LargestArray {

	public static void main(String[] args) {

		List<Integer> list = Arrays.asList(2,10, 20, 25, 63, 96, 57);
		
		
		int ref1 = list.stream().sorted(Collections.reverseOrder()).distinct().skip(2).findFirst().get();
		 System.out.println(ref1);

	}

}
