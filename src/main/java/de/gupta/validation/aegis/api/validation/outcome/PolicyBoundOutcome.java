package de.gupta.validation.aegis.api.validation.outcome;

public sealed interface PolicyBoundOutcome<M> extends Outcome<M> permits RejectedOutcome, ValidatedOutcome
{
	ValidationPolicy policy();
}