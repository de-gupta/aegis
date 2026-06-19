package de.gupta.validation.aegis.api.assessment.assessor;

import de.gupta.validation.aegis.api.assessment.assessment.Assessment;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AssessorFactory")
final class AssessorFactoryTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);
	private static final TestViolation HIGH = new TestViolation("high", Severity.HIGH);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for with()")
	final class ForWith
	{
		@Test
		@DisplayName("returns an assessor that combines the assessment results of the supplied assessments")
		void returnsAnAssessorThatCombinesTheAssessmentResultsOfTheSuppliedAssessments()
		{
			Assessment<String> blankAssessment = value -> value.isBlank()
					? AssessmentResultFactory.with(LOW)
					: AssessmentResultFactory.empty();
			Assessment<String> lengthAssessment = value -> value.trim().length() < 3
					? AssessmentResultFactory.with(HIGH)
					: AssessmentResultFactory.empty();

			var assessor = AssessorFactory.with(List.of(blankAssessment, lengthAssessment));

			var result = assessor.assess(" ");

			assertThat(result.violations())
					.as("combined violations")
					.isEqualTo(Set.of(LOW, HIGH));
		}
	}
}
