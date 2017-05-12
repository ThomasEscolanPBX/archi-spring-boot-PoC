package com.photobox.demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonsRepository extends JpaRepository<Person, Long> {

	// @Param Spring Data REST : Use @Param or compile with -parameters on JDK 8
	// @RequestParam Swagger : paramType=query cf. $Api*Param

	Person findByLastName(@Param("name") @RequestParam("name") String name);

	// @see http://docs.spring.io/spring-data/rest/docs/current/reference/html/#paging-and-sorting
	@ApiImplicitParams({	//FIXME
		@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
						  value = "Results page you want to retrieve (0..N)"),
		@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
						  value = "Number of records per page."),
		@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
						  value = "Sorting criteria in the format: property(,asc|desc). "
								  + "Default sort order is ascending. "
								  + "Multiple sort criteria are supported.")
	})
	Page<Person> findByAddressCity(@Param("city") @RequestParam("city") String city, Pageable p);

}
