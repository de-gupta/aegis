package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.Optional;

record SuccessfulOutcomeImpl<M>(M value, ValidationResult validationResult) implements SuccessfulOutcome<M>
{
	static <M> SuccessfulOutcome<M> of(final M value, final ValidationResult validationResult)
	{
		return Unfolding.beckon(value)
		                .metamorphose(v -> new SuccessfulOutcomeImpl<>(v, validationResult))
		                .decree(ExceptionHelper.iaeFrom("Value may not be null"));
	}

	@Override
	public Optional<M> optionalValue()
	{
		return Optional.of(value);
	}
}