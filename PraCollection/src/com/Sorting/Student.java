package com.Sorting;

public class Student  implements Comparable<Student>{
	 private int rollno;
	    private String name, address;
		public Student(int rollno, String name, String address) {
			super();
			this.rollno = rollno;
			this.name = name;
			this.address = address;
		}
		
		
		public int getRollno() {
			return rollno;
		}
		public void setRollno(int rollno) {
			this.rollno = rollno;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
		@Override
		public String toString() {
			return "Student [rollno=" + rollno + ", name=" + name + ", address=" + address + "]";
		}
		@Override
		public int compareTo(Student o) {
			// TODO Auto-generated method stub
			  return this.rollno-o.rollno;
		}
}
	    


