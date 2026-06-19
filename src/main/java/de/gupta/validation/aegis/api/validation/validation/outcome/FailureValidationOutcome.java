package de.gupta.validation.aegis.api.validation.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;

import java.util.Optional;

public sealed interface FailureValidationOutcome<M> extends ValidationOutcome<M>
		permits FailureValidationOutcomeImpl, RejectedValidationOutcome
{
	@Override
	default boolean isSuccessful()
	{
		return false;
	}

	@Override
	default Optional<M> optionalValue()
	{
		return Optional.empty();
	}

}

record FailureValidationOutcomeImpl<M>(ValidationResult validationResult) implements FailureValidationOutcome<M>
{
	static <M> FailureValidationOutcome<M> of(ValidationResult validationResult)
	{
		return Unfolding.beckon(validationResult)
		                .metamorphose(result -> new FailureValidationOutcomeImpl<M>(result))
		                .decree(ExceptionHelper.iaeFrom("Validation result may not be null"));
	}
}