package com.Assignment;

import java.util.ArrayList;
import java.util.List;

public class ProductMain {

	public static void main(String[] args) {
		List<Product> list = new ArrayList<>();
		
		list.add(new Product(1, "latha"));
		list.add(new Product(2, "sai"));
		list.add(new Product(3, "bhavitha"));
		list.add(new Product(4, "anusha"));
		
		list.stream().sorted().map(Product::getProductName).forEach(System.out::println);

	}

}
