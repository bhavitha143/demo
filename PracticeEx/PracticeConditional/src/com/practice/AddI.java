package com.practice;

public class AddI {
	public static void main(String[] args) {
		
		Add aa=(a,b)->{
			System.out.println(a+b);
			return a+b;
		};
		aa.add(10, 20);

}
}
