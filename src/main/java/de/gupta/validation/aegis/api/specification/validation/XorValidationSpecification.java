package de.gupta.validation.aegis.api.specification.validation;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.XorSpecification;

import java.util.Set;

final class XorValidationSpecification<T> extends CompositeValidationSpecification<T>
		implements ValidationSpecification<T>
{
	private final ValidationSpecification<T> left;
	private final ValidationSpecification<T> right;

	public static <T> ValidationSpecification<T> from(
			final ValidationSpecification<T> left,
			final ValidationSpecification<T> right)
	{
		return new XorValidationSpecification<>(left, right);
	}

	@Override
	public void validate(final T t)
	{
		if (left.isSatisfiedBy(t) && right.isSatisfiedBy(t))
		{
			throw ValidationFailedException.withMessage("Both specifications are satisfied " + Set.of(left, right));
		}
		if (!left.isSatisfiedBy(t) && !right.isSatisfiedBy(t))
		{
			throw ValidationFailedException.withMessage("Neither specification is satisfied " + Set.of(left, right));
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
				if (left.isSatisfiedBy(t) && !right.isSatisfiedBy(t))
				{
					throw ValidationFailedException.withMessage(
							"The specification " + left + " was satisfied, but " + right + " was not.");
				}
				if (!left.isSatisfiedBy(t) && right.isSatisfiedBy(t))
				{
					throw ValidationFailedException.withMessage(
							"The specification " + right + " was satisfied, but " + left + " was not.");
				}

				if (!isSatisfiedBy(t))
				{
					throw new RuntimeException("Something went wrong: Specifications are " + Set.of(left, right));
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
		return XorSpecification.from(left, right).isSatisfiedBy(t);
	}

	private XorValidationSpecification(final ValidationSpecification<T> left,
									   final ValidationSpecification<T> right)
	{
		this.left = left;
		this.right = right;
	}
}