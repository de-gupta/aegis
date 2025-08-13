package de.gupta.validation.aegis.api.validation.time;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AgeSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V> implements ValidationSpecification<T>
{
	private final Function<T, LocalDate> extractor;
	private final int maximumAge;
	private final int minimumAge;

	public static <T, V extends ValidationFailedException> AgeSpecification<T, V> of(
			final Function<T, LocalDate> extractor,
			final int minimumAge,
			final int maximumAge,
			final Supplier<V> exceptionSupplier)
	{
		return new AgeSpecification<>(extractor, minimumAge, maximumAge, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return computeAgeInYears(t) <= maximumAge && computeAgeInYears(t) >= minimumAge;
	}

	private int computeAgeInYears(final T t)
	{
		return Period.between(extractor.apply(t), LocalDate.now()).getYears();
	}

	private AgeSpecification(final Function<T, LocalDate> extractor, final int minimumAge, final int maximumAge,
							 final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
		this.minimumAge = minimumAge;
		this.maximumAge = maximumAge;
	}
}