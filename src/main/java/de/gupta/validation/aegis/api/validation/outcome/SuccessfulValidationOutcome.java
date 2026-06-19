package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.Optional;

public sealed interface SuccessfulValidationOutcome<M> extends ValidationOutcome<M>
		permits SuccessfulValidationOutcomeImpl, ValidatedValidationOutcome
{
	M value();

	@Override
	default boolean isSuccessful()
	{
		return true;
	}
}

record SuccessfulValidationOutcomeImpl<M>(M value, ValidationResult validationResult) implements
		SuccessfulValidationOutcome<M>
{
	static <M> SuccessfulValidationOutcome<M> of(final M value, final ValidationResult validationResult)
	{
		return Unfolding.beckon(value)
		                .metamorphose(v -> new SuccessfulValidationOutcomeImpl<>(v, validationResult))
		                .decree(ExceptionHelper.iaeFrom("Value may not be null"));
	}

	@Override
	public Optional<M> optionalValue()
	{
		return Optional.of(value);
	}
}