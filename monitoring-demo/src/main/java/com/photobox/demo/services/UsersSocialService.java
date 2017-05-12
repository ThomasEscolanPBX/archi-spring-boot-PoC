package com.photobox.demo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.photobox.demo.domain.EventQuote;
import com.photobox.demo.domain.EventQuoteRepository;
import com.photobox.demo.domain.QuoteReference;
import com.photobox.demo.domain.rest.User;
import com.photobox.demo.domain.rest.UserRepository;

@Service @Transactional
@PreAuthorize("isAuthenticated()")
public class UsersSocialService {

	@Autowired
	private EventQuoteRepository quoteDao;
	@Autowired
	private UserRepository userDao;

	private User getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userDao.findByUsername(username);
	}

	public void setLikeToQuote(Long quoteId) {
		EventQuote quote = quoteDao.fetchQuote(quoteId);
		User current = this.getCurrentUser();
		Assert.notNull(current);
		if (quote.getQuoteLikers().contains(current)) {
//			quote.getQuoteLikers().remove(current);
			current.getLikes().remove(quote);
		} else {
//			quote.getQuoteLikers().add(current);
			current.getLikes().add(quote);
		}
//		quoteDao.saveAndFlush(quote);
		userDao.saveAndFlush(current);
	}

	public void setValidationToReference(Long quoteId, Long refId) {
		EventQuote quote = quoteDao.fetchQuote(quoteId);
		User current = this.getCurrentUser();
		Assert.notNull(current);
		SEARCH: {
			for (QuoteReference ref : quote.getQuoteReferences()) {
				if (ref.getId().equals(refId)) {
					if (ref.getValidators().contains(current)) {
//						ref.getValidators().remove(current);
						current.getValidations().remove(ref);
					} else {
//						ref.getValidators().add(current);
						current.getValidations().add(ref);
					}
					break SEARCH;
				}
			}
			throw new ResourceNotFoundException("Reference ID:" + refId + " not found for Quote ID:" + quoteId);
		}
//		quoteDao.saveAndFlush(quote);
		userDao.saveAndFlush(current);
	}
}
