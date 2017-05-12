package com.photobox.demo;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.photobox.demo.domain.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BootWebDemoApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private RestTemplateBuilder builder;

	@Test
	public void testRest() throws Exception {
		ResponseEntity<Person> result = builder.basicAuthorization("test", "test").build()
				.getForEntity("http://localhost:{port}/persons/{id}", Person.class, port, 1);
		assertNotNull(result);
	}
}
