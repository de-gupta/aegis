package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.Validation;

import java.util.function.Supplier;

public final class ValidatorFactory
{
	public static <T> Validator<T> fromValidationSupplier(final Supplier<Validation<T>> validationSupplier)
	{
		return fromValidation(validationSupplier.get());
	}

	public static <T> Validator<T> fromValidation(final Validation<T> validationSpecification)
	{
		return validationSpecification::validate;
	}
}