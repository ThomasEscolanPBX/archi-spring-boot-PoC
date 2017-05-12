package com.photobox.demo.web.dto;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Objects;
import com.photobox.demo.domain.EventQuote;
import com.photobox.demo.domain.rest.User;

import lombok.Getter;

/**
 * Décoration avec les informations "sociales" (favoris et approbations des références)
 * @author tescolan
 */
@Getter
public class EventQuoteDto extends EventQuote {

	private Integer likesCount;
	private Boolean quoteLiked;

	public EventQuoteDto(EventQuote quote) {
		BeanUtils.copyProperties(quote, this);
		
		this.setQuoteReferences(quote.getQuoteReferences().stream()
				.map(ref -> new QuoteReferenceDto(ref))
				.collect(Collectors.toSet()));

		this.likesCount = quote.getQuoteLikers().size();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> liker = quote.getQuoteLikers().stream()
				.filter(l -> Objects.equal(l.getUsername(), auth.getName()))
				.findFirst();
		this.quoteLiked = liker.isPresent();
	}
}
