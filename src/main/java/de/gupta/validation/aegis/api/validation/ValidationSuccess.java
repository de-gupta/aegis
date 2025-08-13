package de.gupta.validation.aegis.api.validation;

record ValidationSuccess(String message) implements ValidationResult
{
	private static final ValidationResult INSTANCE = new ValidationSuccess("Validation Successful");

	static ValidationResult genericSuccess()
	{
		return INSTANCE;
	}

	static ValidationResult withMessage(String message)
	{
		return new ValidationSuccess(message);
	}

	@Override
	public boolean isSuccess()
	{
		return true;
	}
}