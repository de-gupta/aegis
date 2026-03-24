package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

import java.util.Comparator;

public final class ComparisonSpecificationFactory
{
	public static <B, A extends B, T extends B> Specification<T> lessThan(final A threshold,
																		  final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.LESS_THAN.comparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<T> lessThanOrEqualTo(final A threshold,
																				   final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.LESS_THAN_OR_EQUAL.comparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<T> greaterThanOrEqualTo(final A threshold,
																					  final Comparator<B> comparator)
	{
		return SpecificationFactory.from(
				ComparisonType.GREATER_THAN_OR_EQUAL.comparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<T> greaterThan(final A threshold,
																			 final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.GREATER_THAN.comparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<T> notEqual(final A threshold,
																		  final Comparator<B> comparator)
	{
		return SpecificationFactory.from(value -> value != null
				&& threshold != null
				&& !ComparisonType.EQUAL.compare(value, threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<T> equal(final A threshold,
																	   final Comparator<B> comparator)
	{
		return SpecificationFactory.from(ComparisonType.EQUAL.comparisonPredicate(threshold, comparator));
	}

	public static <B, A extends B, T extends B> Specification<T> comparison(final A threshold,
																	   final Comparator<B> comparator,
																			final ComparisonType comparisonType)
	{
		return SpecificationFactory.from(comparisonType.comparisonPredicate(threshold, comparator));
	}

	private ComparisonSpecificationFactory()
	{
	}
}
