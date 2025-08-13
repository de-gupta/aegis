package de.gupta.validation.aegis.api.specification.validation.time;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.TimeComparisonType;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TwoTimestampsSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V> implements ValidationSpecification<T>
{
	private final Function<T, OffsetDateTime> toBeTestedExtractor;
	private final Function<T, OffsetDateTime> referenceExtractor;
	private final TimeComparisonType comparisonType;

	public static <T, V extends ValidationFailedException> TwoTimestampsSpecification<T, V> of(
			final TimeComparisonType comparisonType,
			final Function<T, OffsetDateTime> toBeTestedExtractor,
			final Function<T, OffsetDateTime> referenceExtractor,
			final Supplier<V> exceptionSupplier
	)
	{
		return new TwoTimestampsSpecification<>(exceptionSupplier, toBeTestedExtractor, referenceExtractor,
				comparisonType);
	}

	public static <T, V extends ValidationFailedException> TwoTimestampsSpecification<T, V> fromLocalTime(
			final TimeComparisonType comparisonType,
			final Function<T, LocalDateTime> toBeTestedExtractor,
			final Function<T, LocalDateTime> referenceExtractor,
			final Supplier<V> exceptionSupplier
	)
	{
		Function<T, OffsetDateTime> offsetDateTimeToBeTestedExtractor =
				t -> OffsetDateTime.of(toBeTestedExtractor.apply(t), OffsetDateTime.now().getOffset());
		Function<T, OffsetDateTime> offsetDateTimeReferenceExtractor =
				t -> OffsetDateTime.of(referenceExtractor.apply(t), OffsetDateTime.now().getOffset());

		return new TwoTimestampsSpecification<>(exceptionSupplier, offsetDateTimeToBeTestedExtractor,
				offsetDateTimeReferenceExtractor, comparisonType);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> fromLocalDate(
			final TimeComparisonType comparisonType,
			final Function<T, LocalDate> toBeTestedExtractor,
			final Function<T, LocalDate> referenceExtractor,
			final Supplier<V> exceptionSupplier
	)
	{
		Function<T, OffsetDateTime> offsetDateTimeToBeTestedExtractor =
				t -> OffsetDateTime.of(toBeTestedExtractor.apply(t), LocalDateTime.now().toLocalTime(),
						OffsetDateTime.now().getOffset());
		Function<T, OffsetDateTime> offsetDateTimeReferenceExtractor =
				t -> OffsetDateTime.of(referenceExtractor.apply(t), LocalDateTime.now().toLocalTime(),
						OffsetDateTime.now().getOffset());
		return new TwoTimestampsSpecification<>(exceptionSupplier, offsetDateTimeToBeTestedExtractor,
				offsetDateTimeReferenceExtractor, comparisonType);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return compare(toBeTestedExtractor.apply(t), referenceExtractor.apply(t));
	}

	private boolean compare(final OffsetDateTime testTime, final OffsetDateTime referenceTime)
	{
		return switch (comparisonType)
		{
			case BEFORE -> testTime.isBefore(referenceTime);
			case AFTER -> testTime.isAfter(referenceTime);
			case NOT_BEFORE -> !testTime.isBefore(referenceTime);
			case NOT_AFTER -> !testTime.isAfter(referenceTime);
			case EQUAL -> testTime.isEqual(referenceTime);
			case NOT_EQUAL -> !testTime.isEqual(referenceTime);
		};
	}

	private TwoTimestampsSpecification(final Supplier<V> exceptionSupplier,
									   final Function<T, OffsetDateTime> toBeTestedExtractor,
									   final Function<T, OffsetDateTime> referenceExtractor,
									   final TimeComparisonType comparisonType)
	{
		super(exceptionSupplier);
		this.toBeTestedExtractor = toBeTestedExtractor;
		this.referenceExtractor = referenceExtractor;
		this.comparisonType = comparisonType;
	}
}