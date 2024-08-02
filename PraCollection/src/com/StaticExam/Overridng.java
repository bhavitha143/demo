package com.StaticExam;

class hhh{
	void display() {
		System.out.println("bhavi");
	}
}

class xxx extends hhh{
	void display() {
		System.out.println("sai");
	}
}
public class Overridng extends xxx {
	void display() {
		System.out.println("krishna");
	}

	public static void main(String[] args) {
		 Overridng or = new  Overridng();
		 xxx y = new xxx();
		 hhh z = new hhh();
		 or.display();
		 y.display();
		 z.display();
		 
		

	}

}
