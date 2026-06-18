package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.validation.aegis.api.validation.policy.ValidationPolicy;

public sealed interface PolicyBoundOutcome<M> extends Outcome<M> permits RejectedOutcome, ValidatedOutcome
{
	ValidationPolicy policy();
}