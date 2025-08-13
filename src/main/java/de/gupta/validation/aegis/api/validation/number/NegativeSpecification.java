package de.gupta.validation.aegis.api.validation.number;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class NegativeSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends Number> extractor;

	public static <T, V extends ValidationFailedException> NegativeSpecification<T, V> of(
			final Function<T, ? extends Number> extractor,
			final Supplier<V> exceptionSupplier)
	{
		return new NegativeSpecification<>(extractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return extractor.apply(t).doubleValue() < 0;
	}

	private NegativeSpecification(final Function<T, ? extends Number> extractor, final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
	}
}