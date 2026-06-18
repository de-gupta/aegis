package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Validation")
final class ValidationTest
{
	private static final TestViolation BLANK = new TestViolation("blank", Severity.LOW);
	private static final TestViolation TOO_SHORT = new TestViolation("too short", Severity.HIGH);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for add()")
	final class ForAdd
	{
		@Test
		@DisplayName("combines the results from both validations for the same candidate")
		void combinesTheResultsFromBothValidationsForTheSameCandidate()
		{
			Validation<String> blankValidation = value -> value.isBlank()
					? ValidationResultFactory.with(BLANK)
					: ValidationResultFactory.empty();
			Validation<String> lengthValidation = value -> value.trim().length() < 3
					? ValidationResultFactory.with(TOO_SHORT)
					: ValidationResultFactory.empty();

			var result = blankValidation.add(lengthValidation).validate(" ");

			assertThat(result.violations())
					.as("combined violations")
					.isEqualTo(Set.of(BLANK, TOO_SHORT));
			assertThat(result.highestSeverity())
					.as("combined highest severity")
					.hasValue(Severity.HIGH);
		}
	}
}
