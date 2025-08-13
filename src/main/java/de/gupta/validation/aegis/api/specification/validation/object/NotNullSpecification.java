package de.gupta.validation.aegis.api.specification.validation.object;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class NotNullSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ?> extractor;

	public static <T, V extends ValidationFailedException> NotNullSpecification<T, V> of(final Function<T, ?> extractor,
																						 final Supplier<V> exceptionSupplier)
	{
		return new NotNullSpecification<>(extractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return extractor.apply(t) != null;
	}

	private NotNullSpecification(final Function<T, ?> extractor, final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
	}

}