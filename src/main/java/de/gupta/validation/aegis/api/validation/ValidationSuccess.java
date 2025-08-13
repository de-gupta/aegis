package de.gupta.validation.aegis.api.validation;

public record ValidationSuccess() implements ValidationResult
{
	public static ValidationResult from()
	{
		return new ValidationSuccess();
	}

	@Override
	public boolean isSuccess()
	{
		return true;
	}
}