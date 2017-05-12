package com.photobox.demo.domain.rest;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photobox.demo.domain.EventQuote;
import com.photobox.demo.domain.QuoteReference;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

	@Id @NotBlank
	private String username;
	@NotBlank @JsonIgnore
	private String password;	//TODO encrypt?
	private Boolean enabled = true;

	private String firstName;
	private String lastName;
	@Email
	private String email;

	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<UserRole> roles = new HashSet<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "USER_FAV_QUOTES",
			   joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USERNAME"),
			   inverseJoinColumns = @JoinColumn(name = "QUOTE_ID", referencedColumnName = "ID"))
	private Set<EventQuote> likes = new HashSet<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "USER_VALID_REFS",
			   joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USERNAME"),
			   inverseJoinColumns = @JoinColumn(name = "REF_ID", referencedColumnName = "ID"))
	private Set<QuoteReference> validations = new HashSet<>();

	@Version @NotNull
	private Long version;	// optimistic locking & ETag
}
