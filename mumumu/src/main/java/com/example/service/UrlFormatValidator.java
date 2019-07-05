package com.example.service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlFormatValidator implements ConstraintValidator<UrlFormat, String> {

	@Override
	public final boolean isValid(final String obj, final ConstraintValidatorContext arg1) {
		if (obj == null) {
			return true;
		}
		return GenericValidator.isUrl(obj); //apache
	}
	
	@Override
	public void initialize(final UrlFormat arg) {
		
	}
}
