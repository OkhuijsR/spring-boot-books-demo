package com.example.demo;


import com.example.demo.models.Book;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.print.attribute.standard.Media;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {DemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SpringBootTest
public class LiveTest {

    private static final String API_ROOT = "http://localhost:8080/api/books";

    private Book createRandomBook() {
        Book book = new Book();
        book.setTitle(RandomStringUtils.randomAlphabetic(10));
        book.setAuthor(RandomStringUtils.randomAlphabetic(15));
        return book;
    }

    private String createBookAsUri(Book book) {
        Response response = RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");

    }

    @Test
    public void whenGetAllBooks_thenOK() {
    	System.out.println("Running test 'find all books'");
        Response response = RestAssured.get(API_ROOT);
        System.out.println(response.asString());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
    
    @Test
    public void whenGetBooksByTitle_thenOK() throws JSONException {
    	System.out.println("Running test 'find book by title'");
    	Book book = createRandomBook();
    	createBookAsUri(book);
    	System.out.println("titleSet: " + book.getTitle());
    	System.out.println("authorSet: " + book.getAuthor());
    	
    	Response response = RestAssured.get(API_ROOT + "/title/" + book.getTitle());
    	JSONArray JSONResponseBody = new JSONArray(response.body().asString());
    	System.out.println("result: " + JSONResponseBody);
    	
    	assertEquals(JSONResponseBody.getJSONObject(0).getString("title"), book.getTitle());
    	assertEquals(JSONResponseBody.getJSONObject(0).getString("author"), book.getAuthor());
    	
    	assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    	assertTrue(response.as(List.class).size() == 1);
    }
    
    @Test
    public void whenGetBooksById_thenOK() {
    	System.out.println("Running test 'find book with id'");
    	Book book = createRandomBook();
    	String location = createBookAsUri(book);
    	Response response = RestAssured.get(location);
    	System.out.println("result: " + response.body().asString());
    	assertEquals(response.jsonPath().get("title"), book.getTitle());
    	assertEquals(response.jsonPath().get("author"), book.getAuthor());
    	assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
    
    @Test
    public void whenGetNotExistBookById_thenNotFound() {
    	System.out.println("Running test 'bookId not found'");
    	Response response = RestAssured.get(API_ROOT + "/999999999");
    	assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
    
    @Test
    public void whenCreateNewBook_thenCreated() {
    	System.out.println("Running test 'create new book successful");
    	Book book = createRandomBook();
    	Response response = RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).post(API_ROOT);
    	assertEquals(response.jsonPath().get("title"), book.getTitle());
    	assertEquals(response.jsonPath().get("author"), book.getAuthor());
    	assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }
    
    @Test
    public void whenInvalidBook_thenError() {
    	System.out.println("Running test 'create new book error");
    	Book book = createRandomBook();
    	book.setAuthor(null);
    	Response response = RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).post(API_ROOT);
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
    }
    
    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
    	System.out.println("Running test 'update created book");
    	Book book = createRandomBook();
    	String location = createBookAsUri(book);
    	book.setId(Long.parseLong(location.split("api/books/")[1]));
    	book.setAuthor("newAuthor");
    	Response response = RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).put(location);
    	assertEquals("newAuthor", response.jsonPath().get("author"));
    	assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
    
    @Test
    public void whenDeleteCreatedBook_thenOk() {
    	System.out.println("Running test 'delete book successful'");
    	Book book = createRandomBook();
    	String location = createBookAsUri(book);
    	Response response = RestAssured.delete(location);
    	assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    	response = RestAssured.get(location);
    	assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
    
}
