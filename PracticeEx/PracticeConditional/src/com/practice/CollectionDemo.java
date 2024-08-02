package com.practice;

import java.util.ArrayList;
import java.util.List;

class Student1{
	int rollno;
	String name, address;
	
	Student1(int rollno, String name, String address){ //pass the 3 values in class
		super();
		this.rollno = rollno;
		this.name = name;              // parameter and variables names are same use this
		this.address = address;
	}
}

public class CollectionDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Student1> al = new ArrayList<>();
		
		Student1 s1 = new Student1(1,"bhavvi","Siddipet");
		Student1 s2 = new Student1(2,"krish","bpl");
		Student1 s3 = new Student1(3,"sai","Hyd");
		
		al.add(s1);
		al.add(s2);
		al.add(s3);
		 for(Student1 s:al) {System.out.println(s.rollno+"  " +s.name+" "+s.address);
	}

}
}


	