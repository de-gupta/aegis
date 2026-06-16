package de.gupta.validation.aegis.api.validation.result;

public final class ValidationResultFactory
{
	public static ValidationResult success()
	{
		return SuccessfulValidationResult.INSTANCE;
	}

	public static ValidationResult failure()
	{
		return FailedValidationResult.INSTANCE;
	}

	private ValidationResultFactory()
	{
	}
}