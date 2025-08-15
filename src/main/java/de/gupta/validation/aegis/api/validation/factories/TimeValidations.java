package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import de.gupta.validation.aegis.api.specification.time.OffsetDateTimeSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.ValidationFactory;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TimeValidations
{
	public static <T, V extends RuntimeException> Validation<T> isBefore(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isBefore(thresholdExtractor.apply(t)),
				exceptionSupplier);
	}

	public static <T, V extends RuntimeException> Validation<T> isAfter(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isAfter(thresholdExtractor.apply(t)),
				exceptionSupplier);
	}

	public static <T, V extends RuntimeException> Validation<T> isAtTheSameTimeAs(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isAtTheSameTime(thresholdExtractor.apply(t)),
				exceptionSupplier);
	}

	public static <T, V extends RuntimeException> Validation<T> isNotAfter(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.isNotAfter(thresholdExtractor.apply(t)),
				exceptionSupplier);
	}

	public static <T, V extends RuntimeException> Validation<T> compare(
			final Function<T, OffsetDateTime> extractor,
			final Function<T, OffsetDateTime> thresholdExtractor,
			final ComparisonType comparisonType,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(extractor,
				t -> OffsetDateTimeSpecificationFactory.compare(thresholdExtractor.apply(t), comparisonType),
				exceptionSupplier);
	}

	private TimeValidations()
	{
	}
}