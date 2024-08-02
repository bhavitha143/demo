package com.practice;

import java.util.ArrayList;
import java.util.List;

public class FunctionalInterface { // only one abstract method
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 List<String> names = new ArrayList<>();  
		 
		 names.add("ashwini");
		 names.add("bhavvi");
		 names.add("bhavvi");
		 names.add("yashwini");
		 display(names);
	}
		 
		//list.stream().for(p->System.out.println(list));
		 static void display(List<String> names) {
			 names.stream().filter(p->p.startsWith("b")).forEach(p->System.out.println(p));
			
			 
	}
		 
    
}

