package com.practice;

import java.util.ArrayList;
import java.util.List;

public class Array{
	
	public static void main(String[] args) {   

		
		List<String> list = new ArrayList<>();
		
		list.add("stack");
		list.add("overflow");
		list.add("stack");
		list.add("yahoo");
		list.add("google");
		list.add("msn");
		list.add("MSN");
		list.add("stack");
		list.add("overflow");
		list.add("user");
		
		list.stream().sorted().distinct().forEach(x->System.out.println(x));
	}	
}

		
  
		
		