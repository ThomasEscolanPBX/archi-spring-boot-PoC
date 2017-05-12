package com.photobox.demo;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BootMockDemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSecuredGetEntity() throws Exception {
		final String serviceURL = "/persons/{id}";
		final int personId = 1;

		mockMvc.perform(get(serviceURL, personId))
			.andExpect(status().isUnauthorized());

		mockMvc.perform(get(serviceURL, personId)
				.with(user("test").password("test")))
			.andExpect(status().isOk())
            .andExpect(authenticated().withUsername("test"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(personId)))
			.andExpect(jsonPath("$.firstName", is("foo")))
			.andExpect(jsonPath("$.lastName", is("bar")));
	}

	@Test
	public void testSecuredPostEntity() throws Exception {
		final String serviceURL = "/persons";
		final String payload = "{\"firstName\":\"tac\",\"lastName\":\"tactac\"}";

		mockMvc.perform(post(serviceURL)
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());

		mockMvc.perform(post(serviceURL)
				.with(user("test").password("test"))
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
            .andExpect(authenticated().withUsername("test"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", notNullValue()))
			.andExpect(jsonPath("$.firstName", is("tac")))
			.andExpect(jsonPath("$.lastName", is("tactac")))
			.andExpect(jsonPath("$.createdBy", is("test")))	//TODO @WithMockUser or @WithUserDetails ?
			.andExpect(jsonPath("$.createdDate", notNullValue()))
			.andExpect(jsonPath("$.modifiedBy", is("test")))
			.andExpect(jsonPath("$.modifiedDate", notNullValue()));
	}
}
