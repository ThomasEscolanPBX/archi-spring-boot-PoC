package com.photobox.demo.domain;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photobox.demo.domain.rest.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(exclude = "validators")
public class QuoteReference {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private URL url;

	@JsonIgnore
	@ManyToMany(mappedBy = "validations")
	private Set<User> validators = new HashSet<>();

	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Long createdDate;

	@Version @NotNull
	private Long version;	// optimistic locking & ETag
}
