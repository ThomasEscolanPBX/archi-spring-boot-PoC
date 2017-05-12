package com.photobox.demo;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.photobox.demo.domain.Person;
import com.photobox.demo.domain.PersonsRepository;
import com.photobox.demo.web.PersonsController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PersonsController.class, secure = false)
public class MockDemoApplicationTests {

	@MockBean
	private PersonsRepository repository;
	@MockBean
	private CounterService counterService;	// depends on Actuator

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testDaoMocking() throws Exception {
		Person personStub = new Person();
		personStub.setId(0L);
		personStub.setFirstName("Harry");
		personStub.setLastName("Potter");
		Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(personStub);

		this.mockMvc.perform(get("/persons/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(0)))
			.andExpect(jsonPath("$.firstName", is("Harry")))
			.andExpect(jsonPath("$.lastName", is("Potter")));
	}
}
