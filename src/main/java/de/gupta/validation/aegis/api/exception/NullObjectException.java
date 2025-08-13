package de.gupta.validation.aegis.api.exception;

import java.util.function.Supplier;

public final class NullObjectException extends ValidationFailedException
{
	public static Supplier<ValidationFailedException> fromMessage(String message)
	{
		return () -> withMessage(message);
	}

	public static ValidationFailedException withMessage(String message)
	{
		return new NullObjectException(message);
	}

	private NullObjectException(String message)
	{
		super(message);
	}
}