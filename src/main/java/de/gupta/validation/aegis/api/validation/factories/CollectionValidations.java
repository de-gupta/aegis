package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.collection.CollectionSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.ValidationFactory;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CollectionValidations
{
	public static <T, W, V extends RuntimeException> Validation<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Collection<W> collection,
			final Supplier<V> exceptionSupplier)
	{
		return inCollectionSpecification(elementExtractor, _ -> collection, exceptionSupplier);
	}

	public static <T, W, V extends RuntimeException> Validation<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.fromExtractor(
				elementExtractor,
				t ->
						CollectionSpecificationFactory.inCollection(collectionExtractor.apply(t)),
				exceptionSupplier
		);
	}

	private CollectionValidations()
	{
	}
}