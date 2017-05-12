package com.photobox.demo.domain.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(@Param("name") @RequestParam("name") String username);

	User findByEmail(@Param("email") @RequestParam("email") String email);

}
