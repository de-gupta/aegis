package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.string.StringSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.ValidationFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public final class StringValidations
{
	public static <T, V extends RuntimeException> Validation<T> trimmedStringSpecification(
			final Function<T, String> extractor,
			final Supplier<V> exceptionSupplier)
	{
		return ValidationFactory.from(extractor, StringSpecificationFactory.trimmed(), exceptionSupplier);
	}

	private StringValidations()
	{
	}
}