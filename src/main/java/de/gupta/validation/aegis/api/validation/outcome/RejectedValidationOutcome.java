package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.policy.ValidationPolicy;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

public sealed interface RejectedValidationOutcome<M>
		extends PolicyBoundValidationOutcome<M>, FailureValidationOutcome<M>
		permits RejectedValidationOutcomeImpl
{
}

record RejectedValidationOutcomeImpl<M>(ValidationResult validationResult, ValidationPolicy policy) implements
		RejectedValidationOutcome<M>
{
	static <M> RejectedValidationOutcome<M> of(final ValidationResult validationResult, final ValidationPolicy policy)
	{
		return Unfolding.beckon(validationResult)
		                .interdict(policy::isValid,
								ExceptionHelper.iaeFrom(("Validation result may not be valid under the given policy")))
		                .coronate(result -> new RejectedValidationOutcomeImpl<>(result, policy));
	}
}