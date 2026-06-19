package de.gupta.validation.aegis.api.validation.validation.outcome;

import de.gupta.validation.aegis.api.validation.validation.policy.ValidationPolicy;

public sealed interface PolicyBoundValidationOutcome<M> extends ValidationOutcome<M>
		permits RejectedValidationOutcome, ValidatedValidationOutcome
{
	ValidationPolicy policy();
}