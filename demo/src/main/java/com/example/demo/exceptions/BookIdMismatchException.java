package com.example.demo.exceptions;

public class BookIdMismatchException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1977792537406957391L;

	public BookIdMismatchException() {
        super("URL id does not match the book id");
        System.out.println("BookIdMismatchException was thrown.");
    }
}
