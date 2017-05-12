package com.photobox.demo;

import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.photobox.demo.domain.Person;
import com.photobox.demo.domain.PersonsRepository;
import com.photobox.demo.web.PersonsController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PersonsController.class, secure = false)	// slicing on Persons API
public class PersonsControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PersonsRepository dao;
	@MockBean
	private CounterService service;

	@Test
	public void getPerson() throws Exception {
		Mockito.when(dao.findOne(1L)).thenReturn(Person.builder().id(1L).firstName("foo").lastName("bar").build());

		mockMvc.perform(MockMvcRequestBuilders.get("/persons/{id}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("foo")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("bar")));
	}
}
