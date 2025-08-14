package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

import java.util.Collection;
import java.util.Comparator;

public final class CollectionComparisonSpecificationFactory
{
	public static <B, A extends B, T extends B> Specification<Collection<T>> lessThan(final A threshold, final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.LESS_THAN.allComparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<Collection<T>> lessThanOrEqualTo(final A threshold,
																							   final Comparator<B> comparator)
	{
		return SpecificationFactory.from(
				ComparisonType.LESS_THAN_OR_EQUAL.allComparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<Collection<T>> equal(final A threshold,
																				   final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.EQUAL.allComparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<Collection<T>> greaterThanOrEqualTo(final A threshold,
																								  final Comparator<B> comparator)
	{
		return SpecificationFactory.from(
				ComparisonType.GREATER_THAN_OR_EQUAL.allComparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<Collection<T>> greaterThan(final A threshold,
																						 final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.GREATER_THAN.allComparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<Collection<T>> notEqual(final A threshold,
																					  final Comparator<B> comparator)
	{
		Specification<Collection<T>> equal = equal(threshold, comparator);
		return equal.not();
	}

	private CollectionComparisonSpecificationFactory()
	{
	}
}