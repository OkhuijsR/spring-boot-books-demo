package com.example.demo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.xml.HasXPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	
	@Test
    public void testNamen() {
    	System.out.println("====== STARTING testNamen =====");    	
    	final String url = "http://localhost:8080/";
    	final String responseXPath = "/html/body/h1/span";
    	final List<String> nameList = new ArrayList<>();
    	System.out.println("adding names...");
    	nameList.add("Ricardo");
    	nameList.add("Sjoerd");
    	nameList.add("Willem");
    	for (String name : nameList) {
        	final String urlWithName = url.concat(name);
    		//Response response = RestAssured.get(urlWithName);
    		//System.out.println("returned value: " + response.body().asString());
    		ValidatableResponse validatableResponse = RestAssured.get(urlWithName).then().assertThat().contentType(ContentType.HTML).body(HasXPath.hasXPath(responseXPath, equalTo(name)));
    		Response responseMessage = validatableResponse.extract().response();
    		System.out.println(responseMessage.asString());
    		assertEquals(HttpStatus.OK.value(), responseMessage.getStatusCode());
    	}
    	System.out.println("====== ENDING testNamen =====");
    }

}

