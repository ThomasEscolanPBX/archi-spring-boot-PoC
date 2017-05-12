package com.photobox.demo;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.photobox.demo.domain.Author;
import com.photobox.demo.domain.AuthorRepository;
import com.photobox.demo.domain.rest.Country;
import com.photobox.demo.domain.rest.CountryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoTests {

	@Autowired
	private CountryRepository countryDao;
	@Autowired
	private AuthorRepository personDao;

	@Test
	public void testAddCountry() {
		Country result = countryDao.save(new Country("Tunisie"));
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getId());
		Assert.assertEquals("Tunisie", result.getName());
	}

	@Test
	@Transactional
	public void testAddPerson() {
		Author entity = new Author();
		entity.setName("toto");
		entity.setBirth(LocalDate.now());
		entity.setCountry(countryDao.getOne(1L));
		Author result = personDao.save(entity);
		Assert.assertNotNull(result);
		// data
		Assert.assertNotNull(result.getId());
		Assert.assertEquals("toto", result.getName());
		Assert.assertNotNull(result.getBirth());
		Assert.assertNotNull(result.getCountry());
		// audit
		Assert.assertNotNull(result.getCreatedBy());
		Assert.assertNotNull(result.getCreatedDate());
	}

}
