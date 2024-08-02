package com.Assignment;

public class Fibonacci {

	public static void main(String[] args) {
		
		int n1=0,n2=1,n3,i,count=10;  
		  
		  
		 for(i=1;i<count;++i)//loop starts from 2 because 0 and 1 are already printed  
		 {  
		  n3=n1+n2;  
		  System.out.print(" "+n3);  
		  n1=n2;  
		  n2=n3; 
}
}
}
	
