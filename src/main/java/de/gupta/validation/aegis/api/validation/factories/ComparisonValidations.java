package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.comparison.ComparableSpecificationFactory;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.ValidationFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ComparisonValidations
{
	public static <T, W extends Comparable<W>, V extends RuntimeException> Validation<T> comparisonSpecification(
			final Function<T, ? extends W> extractor, final Function<T, ? extends W> thresholdExtractor,
			final ComparisonType comparisonType,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> ComparableSpecificationFactory.comparisonSpecification(thresholdExtractor.apply(t),
						comparisonType),
				exceptionSupplier);
	}

	private ComparisonValidations()
	{
	}
}