package de.gupta.validation.aegis.api.validation.result;

import de.gupta.validation.aegis.api.validation.validation.outcome.OutcomeFactory;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ValidationOutcome")
final class ValidationOutcomeTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for success outcomes")
	final class ForSuccessValidationOutcomeFactory
	{
		@Test
		@DisplayName("exposes the materialized value directly and through the optional projection")
		void exposesTheMaterializedValueDirectlyAndThroughTheOptionalProjection()
		{
			var validationResult = ValidationResultFactory.empty();
			var result = OutcomeFactory.success("validated-value", validationResult);

			assertThat(result)
					.as("success outcome")
					.satisfies(outcome ->
					{
						assertThat(outcome.isSuccessful())
								.as("success flag")
								.isEqualTo(true);
						assertThat(outcome.optionalValue())
								.as("optional value")
								.isEqualTo(Optional.of("validated-value"));
						assertThat(outcome.value())
								.as("materialized value")
								.isEqualTo("validated-value");
						assertThat(outcome.validationResult())
								.as("validation result")
								.isSameAs(validationResult);
					});
		}

		@Test
		@DisplayName("rejects null materialized values to keep the optional contract honest")
		void rejectsNullMaterializedValuesToKeepTheOptionalContractHonest()
		{
			var validationResult = ValidationResultFactory.empty();

			assertThatThrownBy(() -> OutcomeFactory.success(null, validationResult))
					.as("creating a success outcome with null")
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContainingAll("Value", "null");
		}
	}

	@Nested
	@DisplayName("for failure outcomes")
	final class ForFailureValidationOutcomeFactory
	{
		@Test
		@DisplayName("exposes no materialized value and retains the validation result")
		void exposesNoMaterializedValueAndRetainsTheValidationResult()
		{
			var validationResult = ValidationResultFactory.with(LOW);
			var result = OutcomeFactory.<String>failure(validationResult);

			assertThat(result)
					.as("failure outcome")
					.satisfies(outcome ->
					{
						assertThat(outcome.isSuccessful())
								.as("success flag")
								.isEqualTo(false);
						assertThat(outcome.optionalValue())
								.as("optional value")
								.isEmpty();
						assertThat(outcome.validationResult())
								.as("validation result")
								.isSameAs(validationResult);
					});
		}
	}
}