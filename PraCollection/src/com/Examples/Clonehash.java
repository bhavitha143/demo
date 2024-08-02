package com.Examples;

import java.util.HashSet;

public class Clonehash {  
	public static void main(String[] args) { 
		// Create a empty hash set 
		HashSet<String>set = new HashSet<>();
		// use add() method to add values in the hash 
		
		set.add("Red"); 
		set.add("Green"); 
		set.add("Black"); 
		set.add("White"); 
		set.add("Pink"); 
		set.add("Yellow"); 
		
	System.out.println("before clone: " + set);
	System.out.println("after clone: " + set.clone());// direct we can use clone()method
//	HashSet<String> bs=new HashSet<>();
//	bs=(HashSet<String>)set.clone();
//	System.out.println("after clone"+bs);

	    }

	
	




}
