package de.gupta.validation.aegis.api.specification.validation.collection;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NotEmptySpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends Collection<?>> extractor;

	public static <T, V extends ValidationFailedException> NotEmptySpecification<T, V> of(
			final Function<T, ? extends Collection<?>> extractor,
			final Supplier<V> exceptionSupplier)
	{
		return new NotEmptySpecification<>(extractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return !extractor.apply(t).isEmpty();
	}

	private NotEmptySpecification(final Function<T, ? extends Collection<?>> extractor,
								  final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.extractor = extractor;
	}
}