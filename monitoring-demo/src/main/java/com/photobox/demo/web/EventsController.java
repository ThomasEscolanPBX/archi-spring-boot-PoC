package com.photobox.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.photobox.demo.domain.Event;
import com.photobox.demo.domain.EventRepository;
import com.photobox.demo.services.QuotesService;

@RestController
@RequestMapping("/events")
public class EventsController {

	@Autowired
	private EventRepository repository;
	@Autowired
	private QuotesService service;

	@GetMapping("/all")
	public List<Event> getEvents() {
		return repository.findAll();
	}

	@GetMapping
	public List<Event> getEventsByName(@RequestParam("eventName") String namePattern,
									   @RequestParam(required = false) Long countryId) {
		return service.searchEvent(namePattern, countryId);
	}

	@GetMapping("/country")
	public List<Event> getEventsByCountry(@RequestParam("countryName") String name) {
		return repository.findByCountryName(name);
	}

	@GetMapping("/{id}")
	public Event getEvent(@PathVariable Long id) {
		return repository.findOne(id);
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public Event addEvent(@RequestBody Event event) {
		return repository.save(event);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteEvent(@PathVariable Long id) {
		repository.delete(id);
		Assert.isNull(repository.findOne(id));
	}
}
