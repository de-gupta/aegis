package de.gupta.validation.aegis.api.specification.collection;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

import java.util.Collection;

public final class CollectionSpecificationFactory
{
	public static <T>  Specification<Collection<T>> nonEmpty()
	{
		return SpecificationFactory.inverse(Collection::isEmpty);
	}

	public static <T> Specification<T> inCollection(final Collection<T> collection)
	{
		return SpecificationFactory.from(collection::contains);
	}

	public static <T> Specification<T> notInCollection(final Collection<T> collection)
	{
		return SpecificationFactory.inverse(collection::contains);
	}

	public static <T> Specification<Collection<T>> eachInCollection(final Collection<T> collection)
	{
		return SpecificationFactory.from(collection::containsAll);
	}

	private CollectionSpecificationFactory()
	{
	}
}