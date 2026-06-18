package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

public sealed interface RejectedOutcome<M> extends PolicyBoundOutcome<M>, FailureOutcome<M> permits RejectedOutcomeImpl
{
}

record RejectedOutcomeImpl<M>(ValidationResult validationResult, ValidationPolicy policy) implements RejectedOutcome<M>
{
	static <M> RejectedOutcome<M> of(final ValidationResult validationResult, final ValidationPolicy policy)
	{
		return Unfolding.beckon(validationResult)
		                .interdict(policy::isValid,
								ExceptionHelper.iaeFrom(("Validation result may not be valid under the given policy")))
		                .coronate(result -> new RejectedOutcomeImpl<>(result, policy));
	}
}