package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.Optional;

public sealed interface ValidatedOutcome<M> extends PolicyBound<M>, SuccessfulOutcome<M>
		permits ValidatedOutcomeImpl
{
}

record ValidatedOutcomeImpl<M>(M value, ValidationResult validationResult, ValidationPolicy policy)
		implements ValidatedOutcome<M>
{
	static <M> ValidatedOutcome<M> of(final M value, final ValidationResult validationResult,
	                                  final ValidationPolicy policy)
	{
		return Unfolding.beckon(validationResult)
		                .discern(policy::isValid,
								ExceptionHelper.iaeFrom(("Validation result must be valid under the given policy")))
		                .coronate(result -> new ValidatedOutcomeImpl<>(value, result, policy));
	}

	@Override
	public Optional<M> optionalValue()
	{
		return Optional.of(value);
	}
}