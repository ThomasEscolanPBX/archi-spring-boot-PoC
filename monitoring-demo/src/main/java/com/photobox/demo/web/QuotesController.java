package com.photobox.demo.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.photobox.demo.domain.AuthorRepository;
import com.photobox.demo.domain.EventQuote;
import com.photobox.demo.domain.EventQuoteRepository;
import com.photobox.demo.domain.EventRepository;
import com.photobox.demo.domain.QuoteReference;
import com.photobox.demo.domain.rest.CountryRepository;
import com.photobox.demo.services.QuotesService;
import com.photobox.demo.services.UsersSocialService;
import com.photobox.demo.web.dto.EventQuoteDto;

@RestController
@RequestMapping("/quotes")
public class QuotesController {

	@Autowired
	private AuthorRepository authorDao;
	@Autowired
	private CountryRepository countryDao;
	@Autowired
	private EventRepository eventDao;
	@Autowired
	private EventQuoteRepository quoteDao;

	@Autowired
	private QuotesService quoteService;
	@Autowired
	private UsersSocialService userService;

	@GetMapping
	public List<EventQuote> searchQuotes(@RequestParam("search") String textPattern) {
		List<EventQuote> result = quoteDao.findByQuoteTextLikeOrderByCreatedDateAsc("%" + textPattern + "%");
		//TODO détacher les entités ?
		return result.stream().map(quote -> new EventQuoteDto(quote)).collect(Collectors.toList());
	}

	//TODO using quoteService?
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public EventQuote addQuote(@RequestBody EventQuote quote) {
		if (quote.getQuoteAuthor().getId() != null) {
			quote.setQuoteAuthor(authorDao.findOne(quote.getQuoteAuthor().getId()));
		} else {
			quote.setQuoteAuthor(authorDao.save(quote.getQuoteAuthor()));
		}
		if (quote.getQuoteEvent() != null) {
			if (quote.getQuoteEvent().getId() != null) {
				quote.setQuoteEvent(eventDao.findOne(quote.getQuoteEvent().getId()));
			} else {
				if (quote.getQuoteEvent().getCountry().getId() == null) {
					String countryName = quote.getQuoteEvent().getCountry().getName();
					quote.getQuoteEvent().setCountry(countryDao.findByName(countryName));
				}
				quote.setQuoteEvent(eventDao.save(quote.getQuoteEvent()));
			}
		}
		return quoteDao.save(quote);
	}

	@GetMapping("/{id}")
	public EventQuote getQuote(@PathVariable Long id) {
		return quoteDao.findOne(id);
	}

	@DeleteMapping("/{id}")
	public void removeQuote(@PathVariable Long id) {
		quoteService.removeQuote(id);
		Assert.isNull(quoteDao.findOne(id));
	}

	@PutMapping("/{id}")
	public void setQuoteLike(@PathVariable Long id) {
		userService.setLikeToQuote(id);
	}

	@PostMapping("/{id}/ref")
	public void addQuoteReference(@PathVariable Long id, @RequestBody QuoteReference ref) {
		quoteService.addReferenceToQuote(id, ref);
	}

	@DeleteMapping("/{quoteId}/ref/{refId}")
	public void removeQuoteReference(@PathVariable Long quoteId, @PathVariable Long refId) {
		quoteService.removeReferenceFromQuote(quoteId, refId);
	}

	@PutMapping("/{quoteId}/ref/{refId}")
	public void setQuoteReferenceValidation(@PathVariable Long quoteId, @PathVariable Long refId) {
		userService.setValidationToReference(quoteId, refId);
	}
}
