
package com.Examples;
public class Employee{
	private String name;
	private String gender;
	private  int id;
	private  int age;
	private  String department;
	public Employee(String name, String gender, int id, int age, String department) {
		super();
		this.name = name;
		this.gender = gender;
		this.id = id;
		this.age = age;
		this.department = department;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", gender=" + gender + ", id=" + id + ", age=" + age + ", department="
				+ department + "]";
	}
	
	
	
	
	
	
}