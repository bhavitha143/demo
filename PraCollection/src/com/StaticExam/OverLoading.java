package com.StaticExam;

class Addd{
	static int add(int a,int b) {
		return a+b;
	}
	static int add(int x, int y, int z) { 
		return x+y+z;
	}
}

public class OverLoading {
	
	public static void main(String[] args) {  //we r using static so can't instances of method calling
		
		System.out.println(Addd.add(22, 33));
		System.out.println(Addd.add(66,5, 30));

	}

}
