package com.Examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConvertSetToList {

	public static void main(String[] args) {
		 // Create a empty hash set
		HashSet<String> h_set = new HashSet<String>();
		// use add() method to add values in the hash set
		h_set.add("Red"); 
		h_set.add("Green");
		h_set.add("Black");
		h_set.add("White");
		h_set.add("Pink");
		h_set.add("Yellow");
		h_set.add("null");
		System.out.println("Hash Set: " + h_set); // Create a List from HashSet elements
		List<String> list = new ArrayList<String>(h_set);
		list.add("Yellow");                             // Display ArrayList elements 
		System.out.println("ArrayList: "+ list); 
		


		

	}

}
