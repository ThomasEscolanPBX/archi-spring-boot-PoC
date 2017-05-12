package com.photobox.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.photobox.demo.domain.Author;
import com.photobox.demo.domain.AuthorRepository;
import com.photobox.demo.services.AuthorsService;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

	@Autowired
	private AuthorRepository repository;
	@Autowired
	private AuthorsService service;

	@GetMapping
	public List<Author> getAuthors() {	//TODO search
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Author getAuthor(@PathVariable Long id) {
		return repository.findOne(id);
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public Author addAuthor(@RequestBody Author author) {
		return repository.save(author);
	}

	@PutMapping
	public void mergeAuthors(@RequestParam Long id1, @RequestParam Long id2) {
		service.merge(id1, id2);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteAuthor(@PathVariable Long id) {
		repository.delete(id);
	}
}
