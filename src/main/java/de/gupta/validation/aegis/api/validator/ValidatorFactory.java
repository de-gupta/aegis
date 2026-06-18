package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.Validation;

public final class ValidatorFactory
{
	public static <T> Validator<T> anyOf(final Validation<T> validationSpecification)
	{
		return validationSpecification::validate;
	}
}