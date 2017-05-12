package com.photobox.demo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.photobox.demo.domain.Author;
import com.photobox.demo.domain.AuthorRepository;

@Service @Transactional
@PreAuthorize("hasRole('ADMIN')")
public class AuthorsService {

	@Autowired
	private AuthorRepository authorDao;

	public Author merge(Long authorId1, Long authorId2) {
		Author author1 = authorDao.findOne(authorId1);
		if (author1 == null)	throw new ResourceNotFoundException("Author ID:" + authorId1 + " not found");
		Author author2 = authorDao.findOne(authorId2);
		if (author2 == null)	throw new ResourceNotFoundException("Author ID:" + authorId2 + " not found");

		author1.getQuotes().addAll(author2.getQuotes());
		if (author1.getBirth() == null && author2.getBirth() != null)		author1.setBirth(author2.getBirth());
		if (author1.getDeath() == null && author2.getDeath() != null)		author1.setDeath(author2.getDeath());
		if (author1.getCountry() == null && author2.getCountry() != null)	author1.setCountry(author2.getCountry());
//		author1.version++

		authorDao.delete(author2);
		return authorDao.saveAndFlush(author1);
	}
}
