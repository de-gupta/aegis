package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.exception.NullObjectException;
import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.Specification;

import java.util.function.Supplier;

public abstract class AbstractValidationSpecification<T, V extends ValidationFailedException>
		implements ValidationSpecification<T>
{
	private final Specification<T> specification;
	private final Supplier<V> exceptionSupplier;

	@Override
	public ValidationResult validate(final T t)
	{
		return Unfolding.beckon(t)
						.cleave(specification::isSatisfiedBy,
								_ -> ValidationSuccess.from(),
								_ -> ValidationFailure.from(exceptionSupplier))
						.decree(NullObjectException.fromMessage("Object to validate cannot be null."));
	}

	protected AbstractValidationSpecification(final Specification<T> specification, final Supplier<V> exceptionSupplier)
	{
		this.specification = specification;
		this.exceptionSupplier = exceptionSupplier;
	}
}