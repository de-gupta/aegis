package de.gupta.validation.aegis.api.specification.comparison;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

public enum ComparisonType
{
	LESS_THAN,
	LESS_THAN_OR_EQUAL,
	EQUAL,
	GREATER_THAN_OR_EQUAL,
	GREATER_THAN;

	public <T extends Comparable<T>> Predicate<T> comparisonPredicate(T threshold)
	{
		return t -> compare(t, threshold);
	}

	public <T extends Comparable<T>> boolean compare(T left, T right)
	{
		return compare(left, right, Comparator.naturalOrder());
	}

	public <B, A extends B, T extends B> boolean compare(T value, A threshold, Comparator<B> comparator)
	{
		return switch (this)
		{
			case LESS_THAN -> comparator.compare(value, threshold) < 0;
			case LESS_THAN_OR_EQUAL -> comparator.compare(value, threshold) <= 0;
			case EQUAL -> comparator.compare(value, threshold) == 0;
			case GREATER_THAN_OR_EQUAL -> comparator.compare(value, threshold) >= 0;
			case GREATER_THAN -> comparator.compare(value, threshold) > 0;
		};
	}

	public <B, A extends B, T extends B> Predicate<T> comparisonPredicate(A threshold, Comparator<B> comparator)
	{
		return t -> compare(t, threshold, comparator);
	}

	public <B, A extends B, T extends B> Predicate<Collection<T>> allComparisonPredicate(A threshold,
																						 Comparator<B> comparator)
	{
		return t -> compareAll(t, threshold, comparator);
	}

	public <B, A extends B, T extends B> boolean compareAll(Collection<T> values, A threshold, Comparator<B> comparator)
	{
		return values.stream().allMatch(t -> compare(t, threshold, comparator));
	}

}