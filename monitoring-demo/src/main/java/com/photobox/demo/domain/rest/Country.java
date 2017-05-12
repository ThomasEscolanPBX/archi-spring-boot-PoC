package com.photobox.demo.domain.rest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Data
@NoArgsConstructor	// JPA...
public class Country {

	public Country(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	private String name;

	@Version @NotNull
	private Long version;	// optimistic locking & ETag
}
