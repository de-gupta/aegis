package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.exception.FieldValidationFailedException;
import de.gupta.validation.aegis.api.specification.AndSpecification;

final class AndValidationSpecification<T> extends CompositeValidationSpecification<T>
		implements ValidationSpecification<T>
{
	private final ValidationSpecification<T> firstSpecification;
	private final ValidationSpecification<T> secondSpecification;

	public static <T> ValidationSpecification<T> from(
			final ValidationSpecification<T> firstSpecification,
			final ValidationSpecification<T> secondSpecification)
	{
		return new AndValidationSpecification<>(firstSpecification, secondSpecification);
	}

	@Override
	public void validate(final T t)
	{
		if (!firstSpecification.isSatisfiedBy(t))
		{
			firstSpecification.validate(t);
		}
		if (!secondSpecification.isSatisfiedBy(t))
		{
			secondSpecification.validate(t);
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
					throw FieldValidationFailedException.withMessage(
							"At least one of the specifications " + firstSpecification + " and " + secondSpecification +
									" was satisfied.");
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

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return AndSpecification.from(firstSpecification, secondSpecification).isSatisfiedBy(t);
	}

	private AndValidationSpecification(final ValidationSpecification<T> firstSpecification,
									   final ValidationSpecification<T> secondSpecification)
	{
		this.firstSpecification = firstSpecification;
		this.secondSpecification = secondSpecification;
	}
}