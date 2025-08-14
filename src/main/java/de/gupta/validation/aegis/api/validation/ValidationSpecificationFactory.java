package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.specification.collection.CollectionSpecificationFactory;
import de.gupta.validation.aegis.api.specification.comparison.ComparableSpecificationFactory;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ValidationSpecificationFactory
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

	public static <T, W, V extends RuntimeException> Validation<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Collection<W> collection,
			final Supplier<V> exceptionSupplier)
	{
		return inCollectionSpecification(elementExtractor, _ -> collection, exceptionSupplier);
	}

	public static <T, W, V extends RuntimeException> Validation<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(
				elementExtractor,
				t ->
						CollectionSpecificationFactory.inCollection(collectionExtractor.apply(t)),
				exceptionSupplier
		);
	}

	private ValidationSpecificationFactory()
	{
	}
}