package de.gupta.validation.aegis.api.specification.validation;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;

import java.util.function.Supplier;

public abstract class AbstractValidationSpecification<T, V extends ValidationFailedException>
		implements ValidationSpecification<T>
{
	private final Supplier<V> exceptionSupplier;

	@Override
	public void validate(final T t)
	{
		if (!isSatisfiedBy(t))
		{
			throw exceptionSupplier.get();
		}
	}

	@Override
	public ValidationSpecification<T> not()
	{
		ValidationSpecification<T> original = this;

		return new ValidationSpecification<>()
		{
			@Override
			public void validate(final T t)
			{
				if (!isSatisfiedBy(t))
				{
					String originalMessage = exceptionSupplier.get().getMessage();
					String newMessage = "Negation of the following validation failed: " + originalMessage;
					throw exceptionSupplier.get().setMessage(newMessage);
				}
			}

			@Override
			public ValidationSpecification<T> not()
			{
				return original;
			}

			@Override
			public boolean isSatisfiedBy(final T t)
			{
				return !original.isSatisfiedBy(t);
			}
		};
	}

	protected AbstractValidationSpecification(final Supplier<V> exceptionSupplier)
	{
		this.exceptionSupplier = exceptionSupplier;
	}
}