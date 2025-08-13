package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

public final class ComparableSpecificationFactory
{
	public static <T extends Comparable<T>> Specification<T> lessThan(final T threshold)
	{
		return SpecificationFactory.from(ComparisonType.LESS_THAN.comparisonPredicate(threshold));
	}

	public static <T extends Comparable<T>> Specification<T> lessThanOrEqualTo(final T threshold)
	{
		return SpecificationFactory.from(ComparisonType.LESS_THAN_OR_EQUAL.comparisonPredicate(threshold));
	}

	public static <T extends Comparable<T>> Specification<T> equal(final T threshold)
	{
		return SpecificationFactory.from(ComparisonType.EQUAL.comparisonPredicate(threshold));
	}

	public static <T extends Comparable<T>> Specification<T> greaterThanOrEqualTo(final T threshold)
	{
		return SpecificationFactory.from(ComparisonType.GREATER_THAN_OR_EQUAL.comparisonPredicate(threshold));
	}

	public static <T extends Comparable<T>> Specification<T> greaterThan(final T threshold)
	{
		return SpecificationFactory.from(ComparisonType.GREATER_THAN.comparisonPredicate(threshold));
	}

	public static <T extends Comparable<T>> Specification<T> notEqual(final T threshold)
	{
		return equal(threshold).not();
	}

	private ComparableSpecificationFactory()
	{
	}
}