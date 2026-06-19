package de.gupta.validation.aegis.api.validation.result;

import de.gupta.validation.aegis.api.validation.validation.result.ValidationResultAlgebra;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationResultAlgebra")
final class ValidationResultAlgebraTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);
	private static final TestViolation HIGH = new TestViolation("high", Severity.HIGH);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for zero()")
	final class ForZero
	{
		@Test
		@DisplayName("returns an empty validation result")
		void returnsAnEmptyValidationResult()
		{
			var result = ValidationResultAlgebra.EMPTY_SET_BASED.zero();

			assertThat(result.violations())
					.as("violations")
					.isEmpty();
			assertThat(result.highestSeverity())
					.as("highest severity")
					.isEmpty();
		}
	}

	@Nested
	@DisplayName("for add()")
	final class ForAdd
	{
		@Test
		@DisplayName("delegates to the validation result additive behaviour")
		void delegatesToTheValidationResultAdditiveBehaviour()
		{
			var left = ValidationResultFactory.with(LOW);
			var right = ValidationResultFactory.with(HIGH);

			var result = ValidationResultAlgebra.EMPTY_SET_BASED.add(left, right);

			assertThat(result.violations())
					.as("combined violations")
					.isEqualTo(java.util.Set.of(LOW, HIGH));
			assertThat(result.highestSeverity())
					.as("combined highest severity")
					.hasValue(Severity.HIGH);
		}
	}

	@Nested
	@DisplayName("for ValidationResultFactory.with(Supplier)")
	final class ForValidationResultFactoryWithSupplier
	{
		@Test
		@DisplayName("creates a singleton validation result from the supplied violation")
		void createsASingletonValidationResultFromTheSuppliedViolation()
		{
			var result = ValidationResultFactory.with(() -> HIGH);

			assertThat(result.violations())
					.as("violations")
					.isEqualTo(java.util.Set.of(HIGH));
			assertThat(result.highestSeverity())
					.as("highest severity")
					.hasValue(Severity.HIGH);
		}
	}
}