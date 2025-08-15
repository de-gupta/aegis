package de.gupta.validation.aegis.api.specification.number;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonSpecificationFactory;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;

import java.util.Comparator;

public final class DoubleBasedPotentiallyImpreciseNumberSpecificationFactory
{
	public static <T extends Number> Specification<T> lessThanOrEqualTo(final Number threshold)
	{
		return ComparisonSpecificationFactory.lessThanOrEqualTo(threshold,
				Comparator.comparingDouble(Number::doubleValue));
	}

	public static <T extends Number> Specification<T> equal(final Number threshold)
	{
		return ComparisonSpecificationFactory.equal(threshold, Comparator.comparingDouble(Number::doubleValue));
	}

	public static <T extends Number> Specification<T> greaterThanOrEqualTo(final Number threshold)
	{
		return ComparisonSpecificationFactory.greaterThanOrEqualTo(threshold,
				Comparator.comparingDouble(Number::doubleValue));
	}

	public static <T extends Number> Specification<T> notEqual(final Number threshold)
	{
		return ComparisonSpecificationFactory.notEqual(threshold, Comparator.comparingDouble(Number::doubleValue));
	}

	public static <T extends Number> Specification<T> negative()
	{
		return lessThan(0);
	}

	public static <T extends Number> Specification<T> nonNegative()
	{
		return greaterThanOrEqualTo(0);
	}

	public static <T extends Number> Specification<T> lessThan(final Number threshold)
	{
		return ComparisonSpecificationFactory.lessThan(threshold, Comparator.comparingDouble(Number::doubleValue));
	}

	public static <T extends Number> Specification<T> positive()
	{
		return greaterThan(0);
	}

	public static <T extends Number> Specification<T> nonPositive()
	{
		return lessThanOrEqualTo(0);
	}

	public static <T extends Number> Specification<T> greaterThan(final Number threshold)
	{
		return ComparisonSpecificationFactory.greaterThan(threshold, Comparator.comparingDouble(Number::doubleValue));
	}

	public static <T extends Number> Specification<T> comparison(final Number threshold, final ComparisonType comparisonType)
	{
		return ComparisonSpecificationFactory.comparison(threshold, Comparator.comparingDouble(Number::doubleValue), comparisonType);
	}

	private DoubleBasedPotentiallyImpreciseNumberSpecificationFactory()
	{
	}
}