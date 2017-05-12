package com.photobox.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "jpaAuditor")
public class AuditingConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean
	public AuditorAware<String> jpaAuditor() {
		return () -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				return auth.getName();
			} else {
				logger.warn("User not logged in!");
				return "checked";
			}
		};
	}
}
