package de.gupta.validation.aegis.api.validation.number;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.SpecificationBasedValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EachLessThanSpecification<T, V extends ValidationFailedException>
		extends SpecificationBasedValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends Collection<Number>> extractor;
	private final Number threshold;
	private final Supplier<V> exceptionSupplier;

	public static <T, V extends ValidationFailedException> EachLessThanSpecification<T, V> of(
			final Function<T, ? extends Collection<Number>> extractor,
			final Number threshold,
			final Supplier<V> exceptionSupplier)
	{
		return new EachLessThanSpecification<>(extractor, threshold, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		var elementSpecification = LessThanSpecification.of(Function.identity(), threshold, exceptionSupplier);
		Collection<Number> numbers = extractor.apply(t);
		return numbers.stream().allMatch(elementSpecification::isSatisfiedBy);
	}

	private EachLessThanSpecification(final Function<T, ? extends Collection<Number>> extractor,
									  final Number threshold, final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
		this.threshold = threshold;
		this.exceptionSupplier = exceptionSupplier;
	}
}