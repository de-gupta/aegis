package de.gupta.validation.aegis.api.validation.comparison;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.validation.ComparisonType;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ComparisonSpecification<T, W extends Comparable<W>, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends W> extractor;
	private final Function<T, ? extends W> thresholdExtractor;
	private final ComparisonType comparisonType;

	public static <T, W extends Comparable<W>, V extends ValidationFailedException> ValidationSpecification<T> of(
			final Function<T, ? extends W> extractor,
			final Function<T, ? extends W> thresholdExtractor,
			final ComparisonType comparisonType,
			final Supplier<V> exceptionSupplier)
	{
		return new ComparisonSpecification<>(extractor, thresholdExtractor, comparisonType, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		var comparisonResult = extractor.apply(t).compareTo(thresholdExtractor.apply(t));

		return switch (comparisonType)
		{
			case LESS_THAN_OR_EQUAL -> comparisonResult <= 0;
			case LESS_THAN -> comparisonResult < 0;
			case EQUAL -> comparisonResult == 0;
			case GREATER_THAN_OR_EQUAL -> comparisonResult >= 0;
			case GREATER_THAN -> comparisonResult > 0;
		};
	}

	private ComparisonSpecification(final Function<T, ? extends W> extractor,
									final Function<T, ? extends W> thresholdExtractor,
									final ComparisonType comparisonType,
									final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
		this.thresholdExtractor = thresholdExtractor;
		this.comparisonType = comparisonType;
	}
}