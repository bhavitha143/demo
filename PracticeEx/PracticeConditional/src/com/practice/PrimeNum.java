package com.practice;

import java.util.Scanner;

public class PrimeNum {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int a,i,count=0;  //declaring a variable globaly
  System.out.println("Enter a number");
    a=sc.nextInt();
    for(i=2;i<a;i++) {
    	if(a%i==0) {
    		 
			count++;
    		break;
    	}
    		
	}
    if(count==0)
    	System.out.println("number is prime");
    else {
    	System.out.println("number is not prime");
    }
    	

	}
}



//int a[]= {1,2,5,6,7}
//String a[] = {"7"}   
//String str ="fsnhbc";  //this variable
//String str1="nhtr"     //this string values stored into string constant pool area only
//int a[5][7]



