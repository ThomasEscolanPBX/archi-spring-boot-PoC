package com.photobox.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class MonitoringDemoApplication extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/business").setViewName("business");
		registry.addViewController("/error").setViewName("error");
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/webjars/**")	// see WebSecurityConfig
    		.addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

	public static void main(String[] args) {
		SpringApplication.run(MonitoringDemoApplication.class, args);
	}
}
