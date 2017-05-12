package com.photobox.demo.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.photobox.demo.domain.rest.Country;

import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Event {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDate date;		//TODO year only?
	private String name;
	@ManyToOne(optional = false)
	private Country country;	//TODO city?

	private Boolean accepted = false;

	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Long createdDate;

	@Version @NotNull
	private Long version;	// optimistic locking & ETag
}
