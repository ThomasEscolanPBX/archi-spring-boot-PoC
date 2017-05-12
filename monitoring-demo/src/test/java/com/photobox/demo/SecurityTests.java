package com.photobox.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {

	private static final String APPLICATION_HAL_JSON_UTF8 = "application/hal+json;charset=UTF-8";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSecuredEntity() throws Exception {
		mockMvc.perform(get("/api/countries/1"))
				.andExpect(unauthenticated())
				.andExpect(status().is3xxRedirection());

		MvcResult mvcResult = mockMvc.perform(formLogin().user("test").password("test"))
				.andExpect(authenticated().withUsername("test")
										  .withRoles("ADMIN"))
				.andReturn();
		MockHttpSession session = (MockHttpSession) mvcResult.getRequest().getSession(false);
		mockMvc.perform(get("/api/countries/1")
				.session(session))
			.andExpect(authenticated())	// failure if session not passed
			.andExpect(status().isOk())	// error 403 when CSRF is enabled (see HttpSecurity)
			.andExpect(content().contentType(APPLICATION_HAL_JSON_UTF8))	// HATEOAS special
			.andExpect(jsonPath("$.name", is("France")));

		mockMvc.perform(logout())	//TODO session?
			.andExpect(unauthenticated())
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void testSecuredPostEntity() throws Exception {
		mockMvc.perform(post("/authors")
				.content("{\"name\":\"toto\",\"birth\":\"2016-01-01\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(authenticated().withUsername("test")
									  .withRoles("ADMIN"))
			.andExpect(status().isOk())	// error 403 when CSRF is enabled (see HttpSecurity)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			// data
			.andExpect(jsonPath("$.id", notNullValue()))
			.andExpect(jsonPath("$.name", is("toto")))
			.andExpect(jsonPath("$.birth", is("2016-01-01")))
			// audit
			.andExpect(jsonPath("$.createdBy", is("test")))
			.andExpect(jsonPath("$.createdDate", notNullValue()));
	}
}
