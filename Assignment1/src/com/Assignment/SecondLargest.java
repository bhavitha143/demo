package com.Assignment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecondLargest {

	public static void main(String[] args) {
   List<Integer> list = Arrays.asList(2,3,5,6,1,0);
		
		
		int ref1 = list.stream().sorted(Collections.reverseOrder()).distinct().skip(2).findFirst().get();
		 System.out.println(ref1);

	}


	}


