package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

import java.util.Comparator;
import java.util.function.Function;

public final class ExtractorComparisonSpecificationFactory
{
	public static <B, A extends B, T extends B> Specification<T> lessThan(final A threshold,
																		  final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.LESS_THAN.comparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, P extends B, T> Specification<T> comparisonSpecification(
			final Function<T, P> propertyExtractor,
			final Function<T, A> thresholdExtractor,
			final Comparator<B> comparator,
			final ComparisonType comparisonType)
	{
		return SpecificationFactory.from(t -> comparisonType.compare(propertyExtractor.apply(t), thresholdExtractor.apply(t), comparator));
	}

	private ExtractorComparisonSpecificationFactory()
	{
	}
}