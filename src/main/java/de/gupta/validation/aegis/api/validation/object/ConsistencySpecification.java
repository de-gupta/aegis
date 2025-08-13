package de.gupta.validation.aegis.api.validation.object;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.SpecificationBasedValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ConsistencySpecification<T, V extends ValidationFailedException>
		extends SpecificationBasedValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, Boolean> consistencyChecker;

	public static <T, V extends ValidationFailedException> ConsistencySpecification<T, V> of(
			final Function<T, Boolean> consistencyChecker,
			final Supplier<V> exceptionSupplier)
	{
		return new ConsistencySpecification<>(consistencyChecker, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return consistencyChecker.apply(t);
	}

	private ConsistencySpecification(final Function<T, Boolean> consistencyChecker,
									 final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.consistencyChecker = consistencyChecker;
	}

}