package com.bigid.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bigid.exceptions.BusinessException;
import com.bigid.exceptions.ServerValidationException;
import com.bigid.web.FieldErrorResource;
import com.bigid.web.commands.CustomHttpResponse;

@ControllerAdvice
public class BigIdGlobalControlAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ ServerValidationException.class, BusinessException.class})
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		if(e instanceof ServerValidationException) {
	        ServerValidationException ire = (ServerValidationException) e;
	        List<FieldErrorResource> fieldErrorResources = new ArrayList<>();
	
	        List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
	        for (FieldError fieldError : fieldErrors) {
	            FieldErrorResource fieldErrorResource = new FieldErrorResource();
	            fieldErrorResource.setResource(fieldError.getObjectName());
	            fieldErrorResource.setField(fieldError.getField());
	            fieldErrorResource.setCode(fieldError.getCode());
	            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
	            fieldErrorResources.add(fieldErrorResource);
	        }
	
	        CustomHttpResponse response = new CustomHttpResponse("", fieldErrorResources);
	
	        return handleExceptionInternal(e, response, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	    } else{
	    	CustomHttpResponse response = new CustomHttpResponse("", e.getMessage());
	    	return new ResponseEntity<Object>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
