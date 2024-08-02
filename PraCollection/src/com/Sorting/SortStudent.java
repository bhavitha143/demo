package com.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortStudent {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

        ArrayList<Student> ar = new ArrayList<Student>();
        ar.add(new Student(111, "bbbb", "london"));
        ar.add(new Student(131, "aaaa", "nyc"));
        ar.add(new Student(121, "cccc", "jaipur"));
   Collections.sort(ar);
  // for(int i=0;i<ar.size();i++) {
   for(Student s:ar)
	   System.out.println(s.toString());
   }
}

	
//		 ArrayList<String> al = new ArrayList<String>(); // string sorting
//	        al.add("Geeks For Geeks");
//	        al.add("Friends");
//	        al.add("Dear");
//	        al.add("Is");
//	        al.add("Superb");
//	   Collections.sort(al);
//	   System.out.println(al);
//	   } //ordered
//}
	

