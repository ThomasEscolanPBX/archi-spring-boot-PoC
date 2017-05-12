package com.photobox.demo.domain.rest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "user_roles",
	   uniqueConstraints = @UniqueConstraint(columnNames = {"user_username", "role"}))
@Data
public class UserRole {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	private User user;
	@NotBlank
	private String role;

	@Version @NotNull
	private Long version;	// optimistic locking & ETag
}
