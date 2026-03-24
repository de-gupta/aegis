package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.aletheia.functional.Unfolding;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
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
		return Optional.ofNullable(threshold)
					   .map(t -> (Predicate<T>) v -> compare(v, t))
					   .orElse(_ -> false);
	}

	public <T extends Comparable<T>> boolean compare(final T value, final T threshold)
	{
		return Unfolding.beckon(value)
						.cleave(_ -> threshold != null, v -> compare(v, threshold, Comparator.naturalOrder()),
								_ -> false)
						.rescue(false);
	}

	public <B, A extends B, T extends B> boolean compare(final T value, final A threshold,
														 final Comparator<B> comparator)
	{
		if (value == null || threshold == null)
		{
			return false;
		}

		int result = comparator.compare(value, threshold);
		return switch (this)
		{
			case LESS_THAN -> result < 0;
			case LESS_THAN_OR_EQUAL -> result <= 0;
			case EQUAL -> result == 0;
			case GREATER_THAN_OR_EQUAL -> result >= 0;
			case GREATER_THAN -> result > 0;
		};
	}

	public <B, A extends B, T extends B> Predicate<T> comparisonPredicate(final A threshold,
																		  final Comparator<B> comparator)
	{
		return t -> compare(t, threshold, comparator);
	}

	public <B, A extends B, T extends B> Predicate<Collection<T>> allComparisonPredicate(final A threshold,
																						 final Comparator<B> comparator)
	{
		return t -> compareAll(t, threshold, comparator);
	}

	public <B, A extends B, T extends B> boolean compareAll(final Collection<T> values, final A threshold,
															final Comparator<B> comparator)
	{
		if (values == null || threshold == null)
		{
			return false;
		}

		return values.stream().allMatch(t -> compare(t, threshold, comparator));
	}
}
