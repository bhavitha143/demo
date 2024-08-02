package com.StaticExam;

class Student{
	int id;
	String name;
	static String college = "PRIN"; //declare a variable or method as static
	public Student(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	void display() {
		System.out.println(id+ " " +name+ " " +college+" ");
	}
	
}

public class StaticEx {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		Student s1 = new Student(102,"bhavitha"); // Static: refer to class name it self or comman property all objects
		Student s2 = new Student(103, "sai");
		
		s1.display();
		s2.display();
	}

}
