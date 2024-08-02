package com.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test {
	
	public static void main( String arg []) {
	int arr[] = {1,2,3,1,2,4};  // find the duplicate values
	
	for(int i=0; i<arr.length; i++) {
		for(int j=i+1; j<arr.length; j++) {
			if(arr[i]  == arr[j])
			System.out.println(arr[j]);
		}
	}

}
}
 

 
 
 
 
 
