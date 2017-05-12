package com.photobox.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.data.rest.basePath}")
	private String dataRestPath;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/home", "/webjars/**").permitAll()
				.antMatchers(HttpMethod.GET, dataRestPath + "/countries").authenticated()
				.antMatchers("/swagger-ui.html", dataRestPath + "/**", "/h2-console").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home", true)
				.failureUrl("/login?error")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll();
		http.csrf().disable();	// useless for REST APIs
		http.headers().frameOptions().sameOrigin();	// H2 console fix
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
			.authoritiesByUsernameQuery("SELECT user_username, role FROM user_roles WHERE user_username=?");
	}
}
