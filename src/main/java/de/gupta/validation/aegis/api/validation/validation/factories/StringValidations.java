package de.gupta.validation.aegis.api.validation.validation.factories;

import de.gupta.validation.aegis.api.specification.string.StringSpecificationFactory;
import de.gupta.validation.aegis.api.validation.validation.Validation;
import de.gupta.validation.aegis.api.validation.validation.factories.generic.ValidationFactory;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

public final class StringValidations
{
	public static <T, V extends Violation> Validation<T> trimmedStringSpecification(
			final Function<T, String> extractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.from(extractor, StringSpecificationFactory.trimmed(), violationSupplier);
	}

	private StringValidations()
	{
	}
}