package de.gupta.validation.aegis.api.validation.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.validation.policy.ValidationPolicy;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;

import java.util.Optional;

public sealed interface ValidatedValidationOutcome<M> extends PolicyBoundValidationOutcome<M>,
		SuccessfulValidationOutcome<M>
		permits ValidatedValidationOutcomeImpl
{
}

record ValidatedValidationOutcomeImpl<M>(M value, ValidationResult validationResult, ValidationPolicy policy)
		implements ValidatedValidationOutcome<M>
{
	static <M> ValidatedValidationOutcome<M> of(final M value, final ValidationResult validationResult,
	                                            final ValidationPolicy policy)
	{
		return Unfolding.beckon(validationResult)
		                .discern(policy::isValid,
								ExceptionHelper.iaeFrom(("Validation result must be valid under the given policy")))
		                .coronate(result -> new ValidatedValidationOutcomeImpl<>(value, result, policy));
	}

	@Override
	public Optional<M> optionalValue()
	{
		return Optional.of(value);
	}
}