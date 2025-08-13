package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;

public sealed interface ValidationResult permits ValidationFailure, ValidationSuccess
{
	String message();

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
								ValidationSuccess.genericSuccess(),
								ValidationFailure.withMessage("XOR failed"));
	}

	default ValidationResult not()
	{
		return Unfolding.beckon(this)
						.cleave(ValidationResult::isSuccess,
								ValidationFailure.withMessage("NOT failed"),
								ValidationSuccess.genericSuccess()
						);
	}
}