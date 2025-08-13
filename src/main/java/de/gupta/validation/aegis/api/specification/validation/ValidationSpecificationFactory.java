package de.gupta.validation.aegis.api.specification.validation;

import de.gupta.validation.aegis.api.exception.FieldValidationFailedException;
import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.collection.EachInCollectionSpecification;
import de.gupta.validation.aegis.api.specification.validation.collection.InCollectionSpecification;
import de.gupta.validation.aegis.api.specification.validation.collection.MapNotEmptySpecification;
import de.gupta.validation.aegis.api.specification.validation.collection.NotEmptySpecification;
import de.gupta.validation.aegis.api.specification.validation.comparison.ComparisonSpecification;
import de.gupta.validation.aegis.api.specification.validation.custom.CustomCheckSpecification;
import de.gupta.validation.aegis.api.specification.validation.number.*;
import de.gupta.validation.aegis.api.specification.validation.object.ConsistencySpecification;
import de.gupta.validation.aegis.api.specification.validation.object.NotNullSpecification;
import de.gupta.validation.aegis.api.specification.validation.string.NoLeadingOrTrailingSpacesSpecification;
import de.gupta.validation.aegis.api.specification.validation.string.NotBlankSpecification;
import de.gupta.validation.aegis.api.specification.validation.time.AgeSpecification;
import de.gupta.validation.aegis.api.specification.validation.time.TimeSpecification;
import de.gupta.validation.aegis.api.specification.validation.time.TwoTimestampsSpecification;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ValidationSpecificationFactory
{
	public static <T, W extends Comparable<W>, V extends ValidationFailedException> ValidationSpecification<T> comparisonSpecification(
			final Function<T, ? extends W> extractor, final Function<T, ? extends W> thresholdExtractor,
			final ComparisonType comparisonType,
			final Supplier<V> exceptionSupplier)
	{
		return ComparisonSpecification.of(extractor, thresholdExtractor, comparisonType, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> timeSpecification(
			final Function<T, OffsetDateTime> extractor, final Supplier<V> exceptionSupplier,
			final TemporalComparisonType comparisonType)
	{
		return TimeSpecification.of(extractor, exceptionSupplier, comparisonType);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> timeSpecificationLocalDate(
			final Function<T, LocalDate> extractor, final Supplier<V> exceptionSupplier,
			final TemporalComparisonType comparisonType)
	{
		return TimeSpecification.fromLocalDate(extractor, exceptionSupplier, comparisonType);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> timeComparisonSpecification(
			final TimeComparisonType comparisonType, final Function<T, OffsetDateTime> toBeTestedExtractor,
			final Function<T, OffsetDateTime> referenceExtractor, final Supplier<V> exceptionSupplier)
	{
		return TwoTimestampsSpecification.of(comparisonType, toBeTestedExtractor, referenceExtractor,
				exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> ageSpecification(
			final Function<T, LocalDate> extractor, final int minimumAge, final int maximumAge,
			final Supplier<V> exceptionSupplier)
	{
		return AgeSpecification.of(extractor, minimumAge, maximumAge, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> negativeSpecification(
			final Function<T, ? extends Number> extractor, final Supplier<V> exceptionSupplier)
	{
		return NegativeSpecification.of(extractor, exceptionSupplier);
	}

	public static <T> ValidationSpecification<T> notNullNotBlankNoLeadingOrTrailingSpacesSpecification(
			Function<T, String> extractor, String fieldName)
	{
		return notNullSpecification(extractor,
				FieldValidationFailedException.fromMessage(fieldName + " may not be null")).and(
																								   notBlankSpecification(extractor,
																										   FieldValidationFailedException.fromMessage(fieldName + " may not be blank")))
																						   .and(noLeadingOrTrailingSpacesSpecification(
																								   extractor,
																								   FieldValidationFailedException.fromMessage(
																										   fieldName + " may not have leading or trailing spaces")));
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> notNullSpecification(
			final Function<T, ?> extractor, final Supplier<V> exceptionSupplier)
	{
		return NotNullSpecification.of(extractor, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> notBlankSpecification(
			final Function<T, String> extractor, final Supplier<V> exceptionSupplier)
	{
		return NotBlankSpecification.of(extractor, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> noLeadingOrTrailingSpacesSpecification(
			final Function<T, String> extractor, final Supplier<V> exceptionSupplier)
	{
		return NoLeadingOrTrailingSpacesSpecification.of(extractor, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> mapNotEmptySpecification(
			final Function<T, Map<?, ?>> extractor, final Supplier<V> exceptionSupplier)
	{
		return MapNotEmptySpecification.of(extractor, exceptionSupplier);
	}

	public static <T> ValidationSpecification<T> nullOrNotBlankAndNoLeadingOrTrailingSpacesSpecification(
			Function<T, String> extractor,
			String fieldName)
	{
		return
				(
						notBlankSpecification(extractor,
								FieldValidationFailedException.fromMessage(fieldName + " may not be blank"))
								.and(noLeadingOrTrailingSpacesSpecification(
										extractor,
										FieldValidationFailedException.fromMessage(
												fieldName + " may not have leading or trailing spaces")))).or(
						notNullSpecification(extractor,
								FieldValidationFailedException.fromMessage(fieldName + " may not be null"))
								.not());
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> collectionNotEmptySpecification(
			final Function<T, ? extends Collection<?>> extractor, final Supplier<V> exceptionSupplier)
	{
		return NotEmptySpecification.of(extractor, exceptionSupplier);
	}

	public static <T, W, V extends ValidationFailedException> ValidationSpecification<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Collection<W> collection,
			final Supplier<V> exceptionSupplier)
	{
		return inCollectionSpecification(elementExtractor, _ -> collection, exceptionSupplier);
	}

	public static <T, W, V extends ValidationFailedException> ValidationSpecification<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return InCollectionSpecification.of(elementExtractor, collectionExtractor, exceptionSupplier);
	}

	public static <T, W, V extends ValidationFailedException> ValidationSpecification<T> eachInCollectionSpecification(
			final Function<T, ? extends Collection<W>> elementsExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return EachInCollectionSpecification.of(elementsExtractor, collectionExtractor, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> greaterThanSpecification(
			final Function<T, ? extends Number> extractor, final Number threshold, final Supplier<V> exceptionSupplier)
	{
		return NumberGreaterThanSpecification.of(extractor, threshold, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> lessThanSpecification(
			final Function<T, ? extends Number> extractor, final Number threshold, final Supplier<V> exceptionSupplier)
	{
		return LessThanSpecification.of(extractor, threshold, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> eachGreaterThanSpecification(
			final Function<T, ? extends Collection<Number>> extractor,
			final Number threshold, final Supplier<V> exceptionSupplier)
	{
		return EachGreaterThanSpecification.of(extractor, threshold, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> eachGreaterThanOrEqualToSpecification(
			final Function<T, ? extends Collection<Number>> extractor,
			final Number threshold, final Supplier<V> exceptionSupplier)
	{
		return EachGreaterThanOrEqualToSpecification.of(extractor, threshold, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> eachLessThanSpecification(
			final Function<T, ? extends Collection<Number>> extractor,
			final Number threshold, final Supplier<V> exceptionSupplier)
	{
		return EachLessThanSpecification.of(extractor, threshold, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> consistencySpecification(
			final Function<T, Boolean> consistencyCheck, final Supplier<V> exceptionSupplier)
	{
		return ConsistencySpecification.of(consistencyCheck, exceptionSupplier);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> customCheckSpecification(
			final Function<T, Boolean> customCheck, final Supplier<V> exceptionSupplier)
	{
		return CustomCheckSpecification.of(customCheck, exceptionSupplier);
	}

	private ValidationSpecificationFactory()
	{
	}
}