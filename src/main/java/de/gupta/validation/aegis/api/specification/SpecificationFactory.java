package de.gupta.validation.aegis.api.specification;

import java.util.function.Function;
import java.util.function.Predicate;

public final class SpecificationFactory
{
	public static <T> Specification<T> from(final Predicate<T> predicate)
	{
		return predicate::test;
	}

	public static <T> Specification<T> from(final Function<T, Boolean> function)
	{
		return function::apply;
	}
}