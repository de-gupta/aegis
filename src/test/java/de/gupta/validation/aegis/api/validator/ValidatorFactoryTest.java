package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidatorFactory")
final class ValidatorFactoryTest
{
	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for anyOf()")
	final class ForAnyOf
	{
		@Test
		@DisplayName("returns a validator that delegates directly to the supplied validation")
		void returnsAValidatorThatDelegatesDirectlyToTheSuppliedValidation()
		{
			Validation<String> validation = value -> value.isBlank()
					? ValidationResultFactory.with(new TestViolation("must not be blank", Severity.LOW))
					: ValidationResultFactory.empty();

			var validator = ValidatorFactory.anyOf(validation);
			var result = validator.validate(" ");

			assertThat(result)
					.as("validator result")
					.satisfies(validationResult ->
					{
						assertThat(validationResult.violations())
								.as("violations")
								.containsExactly(new TestViolation("must not be blank", Severity.LOW));
						assertThat(validationResult.highestSeverity())
								.as("highest severity")
								.hasValue(Severity.LOW);
					});
		}
	}
}