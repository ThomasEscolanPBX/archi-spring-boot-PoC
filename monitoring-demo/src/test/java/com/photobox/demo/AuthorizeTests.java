package com.photobox.demo;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizeTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testAdminRights() throws Exception {
		MockHttpSession session1 = login("foo1", "bar");
		this.addQuote(session1);
		this.addRef(session1);

		MockHttpSession session2 = login("test", "test");
		this.removeRef(session2, status().isOk());
	}

	@Test
	public void testQuoterRights() throws Exception {
		MockHttpSession session1 = login("foo1", "bar");
		this.addQuote(session1);

		MockHttpSession session2 = login("foo2", "bar");
		this.addRef(session2);

		this.removeRef(session1, status().isOk());
	}

	@Test
	public void testUserRights() throws Exception {
		MockHttpSession session1 = login("foo1", "bar");
		this.addQuote(session1);
		this.addRef(session1);

		MockHttpSession session2 = login("foo2", "bar");
		this.removeRef(session2, status().is4xxClientError());

		this.removeRef(session1, status().isOk());
	}

	private MockHttpSession login(String username, String password) throws Exception {
		MvcResult mvcResult = mockMvc.perform(formLogin().user(username).password(password))
				.andExpect(authenticated())
				.andReturn();
		return (MockHttpSession) mvcResult.getRequest().getSession(false);
	}

	private void addQuote(MockHttpSession session) throws Exception {
		mockMvc.perform(post("/quotes")
				.content("{\"quoteAuthor\": {\"id\": \"1\"}, \"quoteText\": \"sdfsd gsdgsg sdg\", " +
						 "\"quoteEvent\": {\"country\": {\"name\": \"France\"}, \"name\": \"sdgd poplmlkkljhh fwxfwza\"}}")
				.contentType(MediaType.APPLICATION_JSON)
				.session(session))
			.andExpect(status().isOk());
	}

	private void addRef(MockHttpSession session) throws Exception {
		mockMvc.perform(post("/quotes/1/ref")
				.content("{\"url\": \"http://www.google.fr\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.session(session))
			.andExpect(status().isOk());
	}

	private void removeRef(MockHttpSession session, ResultMatcher statusMatcher) throws Exception {
		mockMvc.perform(delete("/quotes/1/ref/1")
				.contentType(MediaType.APPLICATION_JSON)
				.session(session))
			.andExpect(statusMatcher);
	}
}
