package de.gupta.validation.aegis.api.specification.object;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

import java.util.Objects;
import java.util.function.Predicate;

public final class ObjectSpecificationFactory
{
	public static <T> Specification<T> notNull()
	{
		return SpecificationFactory.from(Objects::nonNull);
	}

	public static <T>  Specification<T> consistent(final Predicate<T> check)
	{
		return SpecificationFactory.from(check);
	}

	private ObjectSpecificationFactory()
	{
	}
}