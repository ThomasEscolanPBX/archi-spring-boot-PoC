package com.photobox.demo.web.dto;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Objects;
import com.photobox.demo.domain.QuoteReference;
import com.photobox.demo.domain.rest.User;

import lombok.Getter;

/**
 * Décoration avec les informations "sociales" (approbations des références)
 * @author tescolan
 */
@Getter
public class QuoteReferenceDto extends QuoteReference {

	private Integer starsCount;
	private Boolean refStarred;

	public QuoteReferenceDto(QuoteReference ref) {
		BeanUtils.copyProperties(ref, this);

		this.starsCount = ref.getValidators().size();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> validator = ref.getValidators().stream()
				.filter(v -> Objects.equal(v.getUsername(), auth.getName()))
				.findFirst();
		this.refStarred = validator.isPresent();
	}
}
