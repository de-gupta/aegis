package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.exception.FieldValidationFailedException;
import de.gupta.validation.aegis.api.specification.OrSpecification;

final class OrValidationSpecification<T> extends CompositeValidationSpecification<T>
		implements ValidationSpecification<T>
{
	private final ValidationSpecification<T> firstSpecification;
	private final ValidationSpecification<T> secondSpecification;

	public static <T> ValidationSpecification<T> from(
			final ValidationSpecification<T> firstSpecification,
			final ValidationSpecification<T> secondSpecification)
	{
		return new OrValidationSpecification<>(firstSpecification, secondSpecification);
	}

	@Override
	public void validate(final T t)
	{
		if (!isSatisfiedBy(t))
		{
			firstSpecification.validate(t);
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
							"Both specifications " + firstSpecification + " nor " + secondSpecification + " was satisfied.");
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
		return OrSpecification.from(firstSpecification, secondSpecification).isSatisfiedBy(t);
	}

	private OrValidationSpecification(final ValidationSpecification<T> firstSpecification,
									  final ValidationSpecification<T> secondSpecification)
	{
		this.firstSpecification = firstSpecification;
		this.secondSpecification = secondSpecification;
	}
}