package de.gupta.validation.aegis.api.validation.outcome;

public sealed interface PolicyBound<M> permits RejectedOutcome, ValidatedOutcome
{
	ValidationPolicy policy();
}