package com.photobox.demo.web;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ IllegalArgumentException.class, InvalidFormatException.class })
	public ResponseEntity<Object> handleIllegalArgument(Exception ex, WebRequest request) {
		return handleReply(ex, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleIllegalAccess(Exception ex, WebRequest request) {
		return handleReply(ex, HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFound(Exception ex, WebRequest request) {
		return handleReply(ex, HttpStatus.NOT_FOUND, request);
	}

	private ResponseEntity<Object> handleReply(Exception ex, HttpStatus status, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return handleExceptionInternal(ex, new AccessDeniedMessage(ex.getMessage()), headers, status, request);
	}

	@Getter @AllArgsConstructor
	static class AccessDeniedMessage {
		private String error;
	}
}
