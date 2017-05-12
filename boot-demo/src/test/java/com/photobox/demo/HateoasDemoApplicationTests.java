package com.photobox.demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.photobox.demo.domain.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HateoasDemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private static final ParameterizedTypeReference<Resource<Person>> typeRef = 
			new ParameterizedTypeReference<Resource<Person>>() {};

	@Test
	public void testEntity() {
		ResponseEntity<Resource<Person>> responseEntity = restTemplate
				.withBasicAuth("test", "test")
				.exchange("/people/{id}", HttpMethod.GET, null, typeRef, 1L);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			Resource<Person> personResource = responseEntity.getBody();
			Person person = personResource.getContent();
			Assert.assertNotNull(person);
			Assert.assertEquals("foo", person.getFirstName());
			Assert.assertEquals("bar", person.getLastName());
		} else {
			Assert.fail("HTTP error code " + responseEntity.getStatusCode());
		}
	}

	@Test
	public void testSearch() {
		ResponseEntity<Resource<Person>> responseEntity = restTemplate
				.withBasicAuth("test", "test")
				.exchange("/people/search/findByLastName?name={name}", HttpMethod.GET, null, typeRef, "bar");

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			Resource<Person> personResource = responseEntity.getBody();
			Person person = personResource.getContent();
			Assert.assertNotNull(person);
			Assert.assertEquals("foo", person.getFirstName());
			Assert.assertEquals("bar", person.getLastName());
		} else {
			Assert.fail("HTTP error code " + responseEntity.getStatusCode());
		}
	}
}
