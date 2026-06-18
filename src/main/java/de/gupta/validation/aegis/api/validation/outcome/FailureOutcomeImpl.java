package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

record FailureOutcomeImpl<M>(ValidationResult validationResult) implements FailureOutcome<M>
{
}