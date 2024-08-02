package com.Examples;

import java.util.ArrayDeque;

public class Arraydeque {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayDeque<String> ad=new ArrayDeque<String>();
		ad.push("aa");//add
		ad.add("dd");
		ad.poll();
		//ad.poll();   //remove first in first out  
		System.out.println(ad);
		    

	}

}
