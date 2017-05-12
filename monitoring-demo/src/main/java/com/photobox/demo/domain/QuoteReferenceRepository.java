package com.photobox.demo.domain;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface QuoteReferenceRepository extends JpaRepository<QuoteReference, Long> {

	Logger logger = LoggerFactory.getLogger(QuoteReferenceRepository.class);

	QuoteReference findByUrl(URL url);

	@PreAuthorize("hasRole('ADMIN') || #quote.createdBy == authentication.name || #ref.createdBy == authentication.name")
	default void checkAccess(@P("quote") EventQuote quote, @P("ref") QuoteReference ref) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().contains("ROLE_ADMIN")) {
			logger.debug("Access rule: ADMIN does what he wants");
		} else if (auth.getName().equals(quote.getCreatedBy())) {
			logger.debug("Access rule: Quoter looks after his own quotes");
		} else if (auth.getName().equals(ref.getCreatedBy())) {
			logger.debug("Access rule: Sourcer can change his mind");
		} else {
			logger.error("Something got wrong in this place!");
		}
	}

}
