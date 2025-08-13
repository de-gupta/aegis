package de.gupta.validation.aegis.api.exception;

import java.util.function.Function;
import java.util.function.Supplier;

public final class FieldValidationFailedException extends ValidationFailedException
{
	public static Function<String, FieldValidationFailedException> forField()
	{
		return FieldValidationFailedException::forField;
	}

	public static FieldValidationFailedException forField(String fieldName)
	{
		return withMessage("Field validation failed for field: " + fieldName);
	}

	public static FieldValidationFailedException withMessage(String message)
	{
		return new FieldValidationFailedException(message);
	}

	public static Supplier<FieldValidationFailedException> fromMessage(String message)
	{
		return () -> withMessage(message);
	}

	@Override
	public FieldValidationFailedException setMessage(final String message)
	{
		return withMessage(message);
	}

	private FieldValidationFailedException(String message)
	{
		super(message);
	}
}