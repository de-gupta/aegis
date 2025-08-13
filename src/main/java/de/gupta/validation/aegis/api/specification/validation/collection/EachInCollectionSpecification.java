package de.gupta.validation.aegis.api.specification.validation.collection;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EachInCollectionSpecification<T, W, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, ? extends Collection<W>> elementsExtractor;
	private final Function<T, ? extends Collection<W>> collectionExtractor;

	public static <T, W, V extends ValidationFailedException> EachInCollectionSpecification<T, W, V> of(
			final Function<T, ? extends Collection<W>> elementsExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return new EachInCollectionSpecification<>(elementsExtractor, collectionExtractor, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return collectionExtractor.apply(t).containsAll(elementsExtractor.apply(t));
	}

	private EachInCollectionSpecification(final Function<T, ? extends Collection<W>> elementsExtractor,
										  final Function<T, ? extends Collection<W>> collectionExtractor,
										  final Supplier<V> exceptionSupplier
	)
	{
		super(exceptionSupplier);
		this.collectionExtractor = collectionExtractor;
		this.elementsExtractor = elementsExtractor;
	}
}