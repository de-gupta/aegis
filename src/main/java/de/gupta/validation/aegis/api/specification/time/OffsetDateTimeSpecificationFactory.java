package de.gupta.validation.aegis.api.specification.time;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;

import java.time.OffsetDateTime;

public final class OffsetDateTimeSpecificationFactory
{
	public static Specification<OffsetDateTime> isBefore(final OffsetDateTime threshold)
	{
		return SpecificationFactory.from(t -> t.isBefore(threshold));
	}

	public static Specification<OffsetDateTime> isNotAfter(final OffsetDateTime threshold)
	{
		return SpecificationFactory.from(t -> !t.isAfter(threshold));
	}

	public static Specification<OffsetDateTime> isAtTheSameTime(final OffsetDateTime threshold)
	{
		return SpecificationFactory.from(t -> t.isEqual(threshold));
	}

	public static Specification<OffsetDateTime> isNotBefore(final OffsetDateTime threshold)
	{
		return SpecificationFactory.from(t -> !t.isBefore(threshold));
	}

	public static Specification<OffsetDateTime> isAfter(final OffsetDateTime threshold)
	{
		return SpecificationFactory.from(t -> t.isAfter(threshold));
	}

	public static Specification<OffsetDateTime> compare(final OffsetDateTime threshold,
														final ComparisonType comparisonType)
	{
		return SpecificationFactory.from(comparisonType.comparisonPredicate(threshold));
	}

	private OffsetDateTimeSpecificationFactory()
	{
	}
}