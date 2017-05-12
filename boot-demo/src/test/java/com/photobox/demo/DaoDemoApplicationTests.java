package com.photobox.demo;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.photobox.demo.domain.Address;
import com.photobox.demo.domain.Person;
import com.photobox.demo.domain.PersonsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoDemoApplicationTests {

	@Autowired
	private PersonsRepository personDao;
	@Autowired
	private EntityManager em;

	@Test
	@Transactional
	public void testDao() {
		Person person = personDao.findByLastName("bar");
		Assert.assertNotNull(person);

		Address address = new Address();
		address.setNumber("10");
		address.setStreet("Downing Street");
		address.setCity("London");
		em.persist(address);
		person.setAddress(address);
		person = personDao.saveAndFlush(person);

		Person updated = personDao.findOne(person.getId());
		Assert.assertEquals(person, updated);
	}
}
