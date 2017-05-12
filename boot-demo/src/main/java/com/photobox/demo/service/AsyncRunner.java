package com.photobox.demo.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.photobox.demo.domain.Person;

@Component
public class AsyncRunner implements ApplicationRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AsyncService service;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//TODO ConcurrentStopWatch

		Future<Person> p1 = service.collect("bar",  (p) -> logger.warn(p.toString()));
		Future<Person> p2 = service.collect("bar1", (p) -> logger.warn(p.toString()));
		Future<Person> p3 = service.collect("bar2", (p) -> logger.warn(p.toString()));

		while (!(p1.isDone() && p2.isDone() && p3.isDone())) {
			Thread.sleep(10);
		}
		logger.warn("DONE!");
	}
}
