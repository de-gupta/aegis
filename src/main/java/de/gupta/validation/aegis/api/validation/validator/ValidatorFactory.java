package de.gupta.validation.aegis.api.validation.validator;

import de.gupta.validation.aegis.api.validation.validation.Validation;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResultFactory;

import java.util.Collection;

public final class ValidatorFactory
{
	public static <T> Validator<T> with(final Collection<Validation<T>> validations)
	{
		return t -> validations.stream()
		                       .map(validation -> validation.validate(t))
		                       .reduce(ValidationResult::add)
		                       .orElse(ValidationResultFactory.empty());
	}
}