package de.gupta.validation.aegis.api.exception;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RequiredFieldNotSetException extends ValidationFailedException
{
	private static final String DEFAULT_MESSAGE = "Required field not set";

	public static Function<String, RequiredFieldNotSetException> forField()
	{
		return RequiredFieldNotSetException::forField;
	}

	private static RequiredFieldNotSetException forField(String fieldName)
	{
		return withMessage("Required field " + fieldName + " not set");
	}

	public static RequiredFieldNotSetException withMessage(final String message)
	{
		return new RequiredFieldNotSetException(message);
	}

	public static Supplier<RequiredFieldNotSetException> fromMessage(final String message)
	{
		return () -> RequiredFieldNotSetException.withMessage(message);
	}

	public static Supplier<RequiredFieldNotSetException> supplier()
	{
		return RequiredFieldNotSetException::withDefaultMessage;
	}

	public static RequiredFieldNotSetException withDefaultMessage()
	{
		return RequiredFieldNotSetException.withMessage(DEFAULT_MESSAGE);
	}

	@Override
	public RequiredFieldNotSetException setMessage(final String message)
	{
		return withMessage(message);
	}

	private RequiredFieldNotSetException(final String message)
	{
		super(message);
	}
}