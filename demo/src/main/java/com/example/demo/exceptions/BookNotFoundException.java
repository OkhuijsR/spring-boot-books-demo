package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6917205916529973303L;

	public BookNotFoundException() {
        super("Book not found");
        System.out.println("BookNotFoundException was thrown.");
    }
}
