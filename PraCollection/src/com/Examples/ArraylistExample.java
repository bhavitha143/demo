package com.Examples;

import java.util.ArrayList;
import java.util.List;

public class ArraylistExample {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		List<String>la=new ArrayList<>();
		la.add("bhavitha");
		la.add("sai");
		la.add("krishna");
		la.add(null);
		System.out.println(la);
		
		la.remove("bhavitha");
		System.out.println(la);
		
		

	}

}
