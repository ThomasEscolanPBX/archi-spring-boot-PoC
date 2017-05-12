package com.photobox.demo.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

public interface EventQuoteRepository extends JpaRepository<EventQuote, Long> {

	List<EventQuote> findByQuoteTextLikeOrderByCreatedDateAsc(String textPattern);

	/**
	 * @throws ResourceNotFoundException when no match found
	 */
	default EventQuote fetchQuote(Long quoteId) {
		EventQuote quote = this.findOne(quoteId);
		if (quote == null)	throw new ResourceNotFoundException("Quote ID:" + quoteId + " not found");
		return quote;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN') || #quote.createdBy == authentication.name")
	void delete(@P("quote") EventQuote entity);

}
