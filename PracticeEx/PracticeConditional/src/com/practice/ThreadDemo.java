package com.practice;

class ThreadDemo extends Thread{
	public void run() {
		System.out.println("run method is excute by jvm");
	}


// public class ThreadDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadDemo td = new ThreadDemo();
		//Thread1 t1 = new Thred1();//creating multiple classes extend thread class create objects call 
		Thread t = new Thread(td);
             t.start();
	}

}
//thread is independent doesn't effect the other threads
//Threads can be used to perform complicated tasks in the background without 
//interrupting the main program.
//There are two ways to create a thread.
//1. extending the Thread class and overriding its run() method:

// like extend above method  
//2.implement the Runnable interface:
/* public class Main implements Runnable {
public void run() {
  System.out.println("This code is running in a thread");
}
}*/
