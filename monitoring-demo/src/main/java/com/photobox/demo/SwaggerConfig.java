package com.photobox.demo;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig {

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return new RepositoryRestConfigurerAdapter() {
			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration) {
				configuration.setRepositoryDetectionStrategy(RepositoryDetectionStrategies.ANNOTATED);
			}
		};
	}

	@Bean
	@Profile("datarest-all")
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.any())
				.build();
	}

	@Bean
	@Profile("datarest-package")
	public Docket apiPackage() {
		return new Docket(DocumentationType.SWAGGER_2).select()
					.apis(or(basePackage("com.photobox.demo.rest"),			// business services
							basePackage("com.photobox.demo.domain.rest")))	// Spring Data Rest FIXME no entity controller
					.paths(PathSelectors.any())
				.build();
	}

	@Bean
	@Profile("datarest-annotation")
	public Docket apiAnnotation() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(or(withClassAnnotation(RestController.class),				// business services
							 withClassAnnotation(RepositoryRestResource.class)))	// Spring Data Rest FIXME no entity controller
					.paths(PathSelectors.any())
				.build();
	}

	@Bean
	@Profile("default")
	public Docket apiPath() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.any())
					.paths(or(ant("/authors/**"), ant("/events/**"), ant("/quotes/**"),	// business services
							  ant("/api/countries/**"), ant("/api/users/**"))) 			// Spring Data Rest
				.build();
	}
}
