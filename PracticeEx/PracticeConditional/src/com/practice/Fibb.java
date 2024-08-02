package com.practice;

public class Fibb {
	
	public static void main(String[] args) {
		int nums=10;
		int secnum=0;
		int thirdnum=1;
		for(int i=1;i<=nums;i++) {
			
			int temp=secnum+thirdnum; //add of privous no.r sum nor
			System.out.println(temp);//swaping
			//traversing for loop
			secnum=thirdnum;
			thirdnum=temp;
		}

}
}
