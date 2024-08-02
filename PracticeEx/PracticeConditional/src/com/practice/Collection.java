package com.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Collection {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		List<String> al = new ArrayList<>();
		al.add("bhavvi");
		al.add("krish");
		al.add("sai");
		al.add("latha");
		System.out.println(al);
		Collections.sort(al); 
		
		Iterator itr= al.iterator(); //using iterating method get the one by one values
		while(itr.hasNext())
			System.out.println(itr.next());
		
//using for loop    
//		for(String s:al) {
//			System.out.println(s);
		
		for(int i=0; i<al.size(); i++) {
		System.out.println(al.get(i));
	}
		
		//Using Stream and filter
		//Output : krish
		al.stream().filter(s->s.contains("krish")).forEach(System.out::println);
		
		//Using method reference
		//output: bhavvi, krish, sai
		al.forEach(System.out::println);
		
		//using Map stream collection
		// hash map stored the key and value pair
		Map<String ,Integer> hm= new HashMap<>(); 
		hm.put("anusha", 1);
		hm.put("aji", 2);
		hm.put("anusha", 3);
		
		hm.entrySet().stream().forEach(x->System.out.println(x));
		
		// entryset() use to get data from map
	}

}

