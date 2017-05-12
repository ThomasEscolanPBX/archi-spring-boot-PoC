package com.photobox.demo.service;

import java.util.Random;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.SuccessCallback;

import com.photobox.demo.domain.Person;
import com.photobox.demo.domain.PersonsRepository;

@Service
@Async
public class AsyncService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Random random = new Random();

	@Autowired
	private PersonsRepository repository;

	public Future<Person> collect(String name, SuccessCallback<Person> success) throws InterruptedException {
		Thread.sleep((long) random.nextFloat() * 1000L);
		AsyncResult<Person> result = new AsyncResult<>(repository.findByLastName(name));
		result.addCallback(success, (t) -> logger.error(t.getMessage()));
		return result;
	}
}
