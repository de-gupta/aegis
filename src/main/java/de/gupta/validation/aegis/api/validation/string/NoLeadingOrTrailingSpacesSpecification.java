package de.gupta.validation.aegis.api.validation.string;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.validation.SpecificationBasedValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class NoLeadingOrTrailingSpacesSpecification<T, V extends ValidationFailedException>
		extends SpecificationBasedValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, String> extractor;

	public static <T, V extends ValidationFailedException> NoLeadingOrTrailingSpacesSpecification<T, V> of(
			final Function<T, String> extractor,
			final Supplier<V> exceptionSupplier)
	{
		return new NoLeadingOrTrailingSpacesSpecification<>(extractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		String value = extractor.apply(t);
		return value != null && value.trim().equals(value);
	}

	private NoLeadingOrTrailingSpacesSpecification(final Function<T, String> extractor,
												   final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
	}
}