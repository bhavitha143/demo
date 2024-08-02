package com.Assignment;

public class CountWords {
	static int countWords(String sentence) {
        // Initialize count to 1 assuming at least one word is present
        int count = 1;
 
        for (int i = 0; i < sentence.length(); ++i) {
            if (sentence.charAt(i) == ' ') {
                count++;
            }
        }
 
        return count;
    }
 
    public static void main(String[] args) {
        String str = "Hello world";
        System.out.println("Number of words: " + countWords(str));
    }

}
