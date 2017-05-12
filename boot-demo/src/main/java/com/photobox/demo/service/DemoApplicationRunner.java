package com.photobox.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.photobox.demo.PhotoboxProperties;
import com.photobox.demo.domain.Person;
import com.photobox.demo.domain.PersonsRepository;

@Component
public class DemoApplicationRunner implements ApplicationRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PersonsRepository repository;

	@Autowired
	private PhotoboxProperties properties;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info(properties.getGreet());
		if (args.containsOption("show")) {
			for (Person p : repository.findAll()) {
				logger.warn(p.toString());
			}
		} else {
			logger.warn("Not required to show :-(");
		}
	}

}
