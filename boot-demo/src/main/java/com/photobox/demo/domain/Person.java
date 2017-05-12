package com.photobox.demo.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "lastName"))
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;
	private String lastName;

	@ManyToOne
	private Address address;

	@Version @NotNull
	private Long version;	// optimistic locking & ETag

	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Long createdDate;

	@LastModifiedBy
	private String modifiedBy;
	@LastModifiedDate
	private Long modifiedDate;
}
