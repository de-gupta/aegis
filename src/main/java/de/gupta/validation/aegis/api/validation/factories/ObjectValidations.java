package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.object.ObjectSpecificationFactory;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.factories.generic.ValidationFactory;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ObjectValidations
{
	public static <T, V extends Violation> Validation<T> notNullSpecification(
			final Function<T, ?> extractor,
			final Supplier<V> violationSupplier)
	{
		return ValidationFactory.from(extractor, ObjectSpecificationFactory.notNull(), violationSupplier);
	}

	private ObjectValidations()
	{
	}
}