package com.Assignment;

@FunctionalInterface  //indicate di interfece is intended to be functionalInterface
interface MyName{
	
	void abstractMethod();
	default void defaultMethod() {
		System.out.println("defaultMethod implement the interface");
	}
	
}
class MyClass implements MyName{
	@Override
	public void abstractMethod() {
	System.out.println("implement the abstractMethod");
	}
	@Override
	 public void defaultMethod() {
		 System.out.println("implement the defaultMethod");
	 
}
	}


public class FunctionalInterfaceDefaultMethod {

	public static void main(String[] args) {
		MyName obj = new MyClass();
		obj.abstractMethod();
		obj.defaultMethod();//call the 2 above ()'s

	}

}
