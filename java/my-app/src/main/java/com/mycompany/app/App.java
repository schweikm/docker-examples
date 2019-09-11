package com.mycompany.app;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Hello World! - " + i);
	    Thread.sleep(1000);
        }
    }
}
