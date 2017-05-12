package com.photobox.demo.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.photobox.demo.domain.rest.Country;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByNameLike(String namePattern);
	List<Event> findByCountryName(String countryName);
	List<Event> findByNameLikeAndCountry(String namePattern, Country country);

}
