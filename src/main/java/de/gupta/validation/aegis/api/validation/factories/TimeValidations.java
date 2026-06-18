package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import de.gupta.validation.aegis.api.specification.time.OffsetDateTimeSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.factories.generic.ValidationFactory;
import de.gupta.validation.aegis.api.violation.Violation;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TimeValidations
{
	public static <T, V extends Violation> Validation<T> isBefore(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isBefore(thresholdExtractor.apply(t)),
				violationSupplier);
	}

	public static <T, V extends Violation> Validation<T> isAfter(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isAfter(thresholdExtractor.apply(t)),
				violationSupplier);
	}

	public static <T, V extends Violation> Validation<T> isAtTheSameTimeAs(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isAtTheSameTime(thresholdExtractor.apply(t)),
				violationSupplier);
	}

	public static <T, V extends Violation> Validation<T> isNotAfter(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isNotAfter(thresholdExtractor.apply(t)),
				violationSupplier);
	}

	public static <T, V extends Violation> Validation<T> compare(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final ComparisonType comparisonType,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.compare(thresholdExtractor.apply(t), comparisonType),
				violationSupplier);
	}

	private TimeValidations()
	{
	}
}