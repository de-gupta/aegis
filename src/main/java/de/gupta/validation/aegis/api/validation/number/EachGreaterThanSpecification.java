package de.gupta.validation.aegis.api.validation.number;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EachGreaterThanSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends Collection<Number>> extractor;
	private final Number threshold;
	private final Supplier<V> exceptionSupplier;

	public static <T, V extends ValidationFailedException> EachGreaterThanSpecification<T, V> of(
			final Function<T, ? extends Collection<Number>> extractor,
			final Number threshold,
			final Supplier<V> exceptionSupplier)
	{
		return new EachGreaterThanSpecification<>(extractor, threshold, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		var elementSpecification = NumberGreaterThanSpecification.of(Function.identity(), threshold, exceptionSupplier);
		Collection<Number> numbers = extractor.apply(t);
		return numbers.stream().allMatch(elementSpecification::isSatisfiedBy);
	}

	private EachGreaterThanSpecification(final Function<T, ? extends Collection<Number>> extractor,
										 final Number threshold, final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
		this.threshold = threshold;
		this.exceptionSupplier = exceptionSupplier;
	}
}