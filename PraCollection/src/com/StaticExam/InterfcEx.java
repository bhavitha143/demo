package com.StaticExam;

public class InterfcEx implements Book {
   public void display() {     // same method( only one method)
	              // abstract method display & have default methods static 
	   
	  System.out.println("subject");
  }

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		Book b1 = new InterfcEx();
		b1.display();
		

	}

}
