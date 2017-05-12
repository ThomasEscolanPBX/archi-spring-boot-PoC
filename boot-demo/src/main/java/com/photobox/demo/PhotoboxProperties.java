package com.photobox.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("com.photobox")
public class PhotoboxProperties {

	@Getter @Setter
	private String greet;
}
