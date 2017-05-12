package com.photobox.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Address {

	@Id
	@GeneratedValue
	private Long id;

	private String street;
	private String number;
	private String city;
	private String state;
	private String zipCode;

	@Version @NotNull
	private Long version;	// optimistic locking & ETag
}
