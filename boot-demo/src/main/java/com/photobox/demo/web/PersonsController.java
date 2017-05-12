package com.photobox.demo.web;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photobox.demo.domain.Person;
import com.photobox.demo.domain.PersonsRepository;

@RestController
@RequestMapping("/persons")
public class PersonsController {

	@Autowired
	private PersonsRepository repository;
	@Autowired
	private CounterService counters;

	@GetMapping
	public Callable<List<Person>> getPersons() {
		Callable<List<Person>> task = new Callable<List<Person>>() {
			@Override
			public List<Person> call() throws Exception {
				Thread.sleep(3000);
				return repository.findAll();
			}
		};
		// do something more, then let go!
		return task;
	}

	@GetMapping("/{id}")
	@Cacheable("neverchangesanyway")
	public Person getPerson(@PathVariable Long id) {
		System.err.println("This is read only API");
		counters.increment("counter.pbx.demo.person.read");
		return repository.findOne(id);
	}

	@PostMapping
	public ResponseEntity<?> addPerson(@RequestBody Person person) throws URISyntaxException {
		try {
			Person entity = repository.save(person);
			return created(new URI("/person/" + entity.getId())).build();
		} catch (ValidationException e) {
			return badRequest().build();
		}
	}
}
