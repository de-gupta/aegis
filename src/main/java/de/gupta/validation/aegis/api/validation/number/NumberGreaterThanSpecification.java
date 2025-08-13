package de.gupta.validation.aegis.api.validation.number;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class NumberGreaterThanSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends Number> extractor;
	private final Number threshold;

	public static <T, V extends ValidationFailedException> NumberGreaterThanSpecification<T, V> of(
			final Function<T, ? extends Number> extractor,
			final Number threshold,
			final Supplier<V> exceptionSupplier)
	{
		return new NumberGreaterThanSpecification<>(extractor, threshold, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return extractor.apply(t).doubleValue() > threshold.doubleValue();
	}

	private NumberGreaterThanSpecification(final Function<T, ? extends Number> extractor, final Number threshold,
										   final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
		this.threshold = threshold;
	}
}