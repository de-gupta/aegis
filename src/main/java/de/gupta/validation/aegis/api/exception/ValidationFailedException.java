package de.gupta.validation.aegis.api.exception;


import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.Set;
import java.util.function.Supplier;

public class ValidationFailedException extends RuntimeException
{
	public static ValidationFailedException withMessage(final String message)
	{
		return new ValidationFailedException(message);
	}

	public static Supplier<? extends ValidationFailedException> fromMessage(final String message)
	{
		return () -> new ValidationFailedException(message);
	}

	public ValidationFailedException setMessage(final String message)
	{
		return new ValidationFailedException(message);
	}

	protected ValidationFailedException(final String message)
	{
		super(message);
	}

	private <T> ValidationFailedException(final String message, final Set<ValidationSpecification<T>> validations)
	{
		super(message);
	}
}