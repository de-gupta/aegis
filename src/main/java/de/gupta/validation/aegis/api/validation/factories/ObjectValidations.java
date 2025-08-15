package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.object.ObjectSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.ValidationFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ObjectValidations
{
	public static <T, V extends RuntimeException> Validation<T> notNullSpecification(
			final Function<T, ?> extractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.from(extractor, ObjectSpecificationFactory.notNull(), exceptionSupplier);
	}

	private ObjectValidations()
	{
	}
}