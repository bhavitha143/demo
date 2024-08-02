package com.Examples;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Employeemain {
	
	    public static void main(String[] args) {
	        
	List<Employee> emp = new ArrayList<>();
	emp.add(new Employee("ashwini", "female" , 1, 25, "HR"));
	emp.add(new Employee("sai", "male", 2, 23, "IT"));
	emp.add(new Employee("bhavitha","female", 5, 24, "HR"));
	
	// print the only distict department
	emp.stream().map(Employee::getDepartment).distinct().forEach(System.out::println);
	
	//print the name of emp // "Employee::getName" like this called method refernce 
	emp.stream().map(Employee::getName).forEach(System.out::println);
	
	//print count of employees in each department 
//using return type of this will be map of String , Long ->here department is string coutning method is long
	Map<String, Long> empCount = emp.stream()
			.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
	System.out.println(empCount);
	
/* averg of for each gender	
	gender is string and avrg datatype is Double
	fst i'm grouping by the gender female cmng to 1 group male will be cmng to 1 grup */
	
	Map<String, Double> avrgAge = emp.stream()
		.collect(Collectors.groupingBy(Employee::getGender, Collectors.averagingInt(Employee::getAge)));
	System.out.println(avrgAge); 
	

//	for(int i=0;i<e.size();i++)
//	{
//	    System.out.print(" " +e.get(i).getName()); //i.getId like

//	}
	    }

	}




