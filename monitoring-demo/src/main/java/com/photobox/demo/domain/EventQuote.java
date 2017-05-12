package com.photobox.demo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
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
@EqualsAndHashCode(exclude = "quoteLikers")
public class EventQuote {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	private Author quoteAuthor;

	@NotEmpty
	private String quoteText;

	@ManyToOne
	private Event quoteEvent;
	@OneToMany(orphanRemoval = true)
	private Set<QuoteReference> quoteReferences = new HashSet<>();

	@JsonIgnore
	@ManyToMany(mappedBy = "likes")
	private Set<User> quoteLikers = new HashSet<>();

	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Long createdDate;

	@Version @NotNull
	private Long version; // optimistic locking & ETag
}
