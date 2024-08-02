package com.Examples;

import java.util.ArrayList;

import java.util.List;



    
public class MarksGreaterThan80 {

	public static void main(String[] args) {
		List<Student> list = new ArrayList<>(); 
		
		list.add(new Student(1, "bhavi", 60, 21 ));
		list.add(new Student(2, "sai", 85, 23 ));
		list.add(new Student(3, "latha", 90,25));
		
    //print each student name and marks using foreach

		list.stream().filter(x->x.getMarks()>80)
		.forEach(x -> System.out.println(x.getName() + ":"+ x.getMarks()));

}
}
class Student {

	private int rollNo;
	private String name;
	private int marks;
	private int age;
	
	public Student(int rollNo, String name, int marks, int age) {
		super();
		this.rollNo = rollNo;
		this.name = name;
		this.marks = marks;
		this.age = age;
		
	}

	public int getRollNo() {
		return rollNo;
	}

	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
	

