package de.gupta.validation.aegis.api.validation.time;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.validation.TemporalComparisonType;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TimeSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V> implements ValidationSpecification<T>
{
	private final Function<T, OffsetDateTime> extractor;
	private final TemporalComparisonType comparisonType;

	public static <T, V extends ValidationFailedException> TimeSpecification<T, V> of(
			final Function<T, OffsetDateTime> extractor, final Supplier<V> exceptionSupplier,
			final TemporalComparisonType comparisonType)
	{
		return new TimeSpecification<>(extractor, exceptionSupplier, comparisonType);
	}

	public static <T, V extends ValidationFailedException> TimeSpecification<T, V> fromLocalTime(
			final Function<T, LocalDateTime> extractor, final Supplier<V> exceptionSupplier,
			final TemporalComparisonType comparisonType)
	{
		Function<T, OffsetDateTime> offsetDateTimeExtractor =
				t -> OffsetDateTime.of(extractor.apply(t), OffsetDateTime.now().getOffset());
		return new TimeSpecification<>(offsetDateTimeExtractor, exceptionSupplier, comparisonType);
	}

	public static <T, V extends ValidationFailedException> ValidationSpecification<T> fromLocalDate(
			final Function<T, LocalDate> extractor, final Supplier<V> exceptionSupplier,
			final TemporalComparisonType comparisonType)
	{
		Function<T, OffsetDateTime> offsetDateTimeExtractor =
				t -> OffsetDateTime.of(extractor.apply(t), LocalDateTime.now().toLocalTime(),
						OffsetDateTime.now().getOffset());
		return new TimeSpecification<>(offsetDateTimeExtractor, exceptionSupplier, comparisonType);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return compareToNow(extractor.apply(t));
	}

	private boolean compareToNow(final OffsetDateTime time)
	{
		return switch (comparisonType)
		{
			case PAST -> time.isBefore(OffsetDateTime.now());
			case PAST_OR_PRESENT -> !time.isAfter(OffsetDateTime.now());
			case FUTURE -> time.isAfter(OffsetDateTime.now());
			case FUTURE_OR_PRESENT -> !time.isBefore(OffsetDateTime.now());
		};
	}

	private TimeSpecification(final Function<T, OffsetDateTime> extractor, final Supplier<V> exceptionSupplier,
							  final TemporalComparisonType comparisonType)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
		this.comparisonType = comparisonType;
	}
}