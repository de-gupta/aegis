package de.gupta.validation.aegis.api.assessment.assessment;

import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Assessment")
final class AssessmentTest
{
	private static final TestViolation QUARANTINE = new TestViolation("quarantine", Severity.MEDIUM);
	private static final TestViolation REJECT = new TestViolation("reject", Severity.HIGH);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for add()")
	final class ForAdd
	{
		@Test
		@DisplayName("combines the results from both assessments for the same candidate")
		void combinesTheResultsFromBothAssessmentsForTheSameCandidate()
		{
			Assessment<String> quarantineAssessment = value -> value.isBlank()
					? AssessmentResultFactory.with(QUARANTINE)
					: AssessmentResultFactory.empty();
			Assessment<String> rejectionAssessment = value -> value.trim().length() < 3
					? AssessmentResultFactory.with(REJECT)
					: AssessmentResultFactory.empty();

			var result = quarantineAssessment.add(rejectionAssessment).assess(" ");

			assertThat(result.violations())
					.as("combined violations")
					.isEqualTo(Set.of(QUARANTINE, REJECT));
			assertThat(result.highestSeverity())
					.as("combined highest severity")
					.hasValue(Severity.HIGH);
		}
	}
}
