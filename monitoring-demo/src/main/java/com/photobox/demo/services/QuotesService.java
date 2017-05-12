package com.photobox.demo.services;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.photobox.demo.domain.Event;
import com.photobox.demo.domain.EventQuote;
import com.photobox.demo.domain.EventQuoteRepository;
import com.photobox.demo.domain.EventRepository;
import com.photobox.demo.domain.QuoteReference;
import com.photobox.demo.domain.QuoteReferenceRepository;
import com.photobox.demo.domain.rest.Country;
import com.photobox.demo.domain.rest.CountryRepository;

@Service @Transactional
@PreAuthorize("isAuthenticated()")
public class QuotesService {

	@Autowired
	private CountryRepository countryDao;
	@Autowired
	private QuoteReferenceRepository refDao;
	@Autowired
	private EventQuoteRepository quoteDao;
	@Autowired
	private EventRepository eventDao;

	public void removeQuote(Long quoteId) {
		EventQuote quote = quoteDao.fetchQuote(quoteId);
		quoteDao.delete(quote);	// see access control in repository
	}

	public void addReferenceToQuote(Long quoteId, QuoteReference ref) {
		EventQuote quote = quoteDao.fetchQuote(quoteId);
		QuoteReference tmp = refDao.findByUrl(ref.getUrl());
		if (tmp != null) {
			ref = tmp;	// reuse
		} else {
			ref = refDao.save(ref);
		}
		quote.getQuoteReferences().add(ref);
		quoteDao.saveAndFlush(quote);
	}

	public void removeReferenceFromQuote(Long quoteId, Long refId) {
		EventQuote quote = quoteDao.fetchQuote(quoteId);
		SEARCH: {
			Iterator<QuoteReference> iterator = quote.getQuoteReferences().iterator();
			while (iterator.hasNext()) {
				QuoteReference ref = iterator.next();
				if (ref.getId().equals(refId)) {
					refDao.checkAccess(quote, ref);
					iterator.remove();	// cascading for orphans?
					break SEARCH;
				}
			}
			throw new ResourceNotFoundException("Reference ID:" + refId + " not found for Quote ID:" + quoteId);
		}
		quoteDao.saveAndFlush(quote);
	}

	public List<Event> searchEvent(String namePattern, Long countryId) {
		if (countryId == null) {
			return eventDao.findByNameLike(namePattern);
		} else {
			Country country = countryDao.findOne(countryId);
			if (country == null)	throw new ResourceNotFoundException("Country ID:" + countryId + " not found");
			return eventDao.findByNameLikeAndCountry(namePattern, country);
		}
	}
}
