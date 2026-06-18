package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.collection.CollectionSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.factories.generic.ValidationFactory;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CollectionValidations
{
	public static <T, W, V extends Violation> Validation<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Collection<W> collection,
			final Supplier<V> violationSupplier)
	{
		return inCollectionSpecification(elementExtractor, _ -> collection, violationSupplier);
	}

	public static <T, W, V extends Violation> Validation<T> inCollectionSpecification(
			final Function<T, W> elementExtractor,
			final Function<T, ? extends Collection<W>> collectionExtractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.fromExtractor(
				elementExtractor,
				t ->
						CollectionSpecificationFactory.inCollection(collectionExtractor.apply(t)),
				violationSupplier
		);
	}

	private CollectionValidations()
	{
	}
}