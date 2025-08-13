package de.gupta.validation.aegis.api.exception;


import java.util.function.Supplier;

public class ValidationFailedException extends RuntimeException
{
	public static Supplier<? extends ValidationFailedException> fromMessage(final String message)
	{
		return () -> withMessage(message);
	}

	public static ValidationFailedException withMessage(final String message)
	{
		return new ValidationFailedException(message);
	}

	protected ValidationFailedException(final String message)
	{
		super(message);
	}
}