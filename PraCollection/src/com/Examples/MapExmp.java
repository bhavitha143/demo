package com.Examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapExmp {

	public static void main(String[] args) {
		HashMap<String,String> hh=new HashMap<>();
		hh.put("Ashwini","challandula");
		hh.put("Mallesh","Chelukala");
		System.out.println(hh);
		
		Set<Map.Entry<String,String>> set=hh.entrySet();
		for(Map.Entry<String,String> me:set)
		{
		    System.out.println(me.getKey());
		    System.out.println(me.getValue());
		}

		    
		    }
		

	}


