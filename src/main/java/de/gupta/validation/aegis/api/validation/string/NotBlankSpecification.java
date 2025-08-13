package de.gupta.validation.aegis.api.validation.string;

import de.gupta.commons.utility.string.StringSanitizationUtility;
import de.gupta.validation.aegis.api.validation.SpecificationBasedValidationSpecification;
import de.gupta.validation.aegis.api.validation.ValidationResult;
import de.gupta.validation.aegis.api.validation.ValidationSpecification;

import java.util.function.Function;

public record NotBlankSpecification<T>(Function<T, String> extractor) implements ValidationSpecification<T>
{
	public static <T> NotBlankSpecification<T> of(final Function<T, String> extractor)
	{
		return new NotBlankSpecification<>(extractor);
	}

	@Override
	public ValidationResult validate(final T t)
	{
		return SpecificationBasedValidationSpecification.of(
				extractor.andThen(StringSanitizationUtility::isStringNonBlank)::apply).validate(t);
	}
}