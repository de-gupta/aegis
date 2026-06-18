package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.Optional;

public sealed interface FailureOutcome<M> extends Outcome<M> permits FailureOutcomeImpl, RejectedOutcome
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

record FailureOutcomeImpl<M>(ValidationResult validationResult) implements FailureOutcome<M>
{
	static <M> FailureOutcome<M> of(ValidationResult validationResult)
	{
		return Unfolding.beckon(validationResult)
		                .metamorphose(result -> new FailureOutcomeImpl<M>(result))
		                .decree(ExceptionHelper.iaeFrom("Validation result may not be null"));
	}
}