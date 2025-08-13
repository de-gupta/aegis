package de.gupta.validation.aegis.api.specification.validation.string;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class NotBlankSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, String> extractor;

	public static <T, V extends ValidationFailedException> NotBlankSpecification<T, V> of(
			final Function<T, String> extractor,
			final Supplier<V> exceptionSupplier)
	{
		return new NotBlankSpecification<>(extractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		String value = extractor.apply(t);
		return value != null && !value.isBlank();
	}

	private NotBlankSpecification(final Function<T, String> extractor, final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
	}
}