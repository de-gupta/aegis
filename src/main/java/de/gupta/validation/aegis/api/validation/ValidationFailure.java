package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;

import java.util.function.Supplier;

public record ValidationFailure<V extends ValidationFailedException>(Supplier<V> exceptionSupplier)
		implements ValidationResult
{
	public static <V extends ValidationFailedException> ValidationResult from(final Supplier<V> exceptionSupplier)
	{
		return new ValidationFailure<>(exceptionSupplier);
	}

	@Override
	public boolean isSuccess()
	{
		return false;
	}
}