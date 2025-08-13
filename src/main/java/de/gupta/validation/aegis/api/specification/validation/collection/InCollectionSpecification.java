package de.gupta.validation.aegis.api.specification.validation.collection;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class InCollectionSpecification<T, W, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, W> elementExtractor;
	private final Function<T, ? extends Collection<W>> collectionExtractor;

	public static <T, W, V extends ValidationFailedException> InCollectionSpecification<T, W, V> of(
			final Function<T, W> elementExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return new InCollectionSpecification<>(elementExtractor, collectionExtractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return collectionExtractor.apply(t).contains(elementExtractor.apply(t));
	}

	private InCollectionSpecification(final Function<T, W> elementExtractor,
									  final Function<T, ? extends Collection<W>> collectionExtractor,
									  final Supplier<V> exceptionSupplier
	)
	{
		super(exceptionSupplier);
		this.collectionExtractor = collectionExtractor;
		this.elementExtractor = elementExtractor;
	}
}