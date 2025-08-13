package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.exception.CompositeValidationFailureException;

public sealed interface ValidationResult permits ValidationFailure, ValidationSuccess
{
	default ValidationResult or(ValidationResult other)
	{
		return Unfolding.beckon(this)
						.cleave(ValidationResult::isSuccess, this, other);
	}

	boolean isSuccess();

	default ValidationResult and(ValidationResult other)
	{
		return Unfolding.beckon(this)
						.cleave(ValidationResult::isSuccess, other, this);
	}

	default ValidationResult xor(ValidationResult other)
	{
		return Unfolding.beckon(this)
						.cleave(t -> t.isSuccess() ^ other.isSuccess(),
								ValidationSuccess.from(),
								ValidationFailure.from(
										CompositeValidationFailureException.fromMessage("XOR failed"))
						);
	}

	default ValidationResult not()
	{
		return Unfolding.beckon(this)
						.cleave(ValidationResult::isSuccess,
								ValidationFailure.from(
										CompositeValidationFailureException.fromMessage("NOT failed")),
								ValidationSuccess.from()
						);
	}
}