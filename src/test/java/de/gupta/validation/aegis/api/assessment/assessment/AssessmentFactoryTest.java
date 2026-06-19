package de.gupta.validation.aegis.api.assessment.assessment;

import de.gupta.validation.aegis.api.assessment.assessment.factories.generic.AssessmentFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AssessmentFactory")
final class AssessmentFactoryTest
{
	private static final TestViolation TOO_SHORT = new TestViolation("too short", Severity.HIGH);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for from()")
	final class ForFrom
	{
		@Test
		@DisplayName("creates an assessment from a specification and property extractor")
		void createsAnAssessmentFromASpecificationAndPropertyExtractor()
		{
			var assessment = AssessmentFactory.from(String::trim, value -> value.length() >= 3, () -> TOO_SHORT);

			var result = assessment.assess(" a ");

			assertThat(result.violations())
					.as("violations")
					.containsExactly(TOO_SHORT);
		}
	}

	@Nested
	@DisplayName("for fromExtractor()")
	final class ForFromExtractor
	{
		@Test
		@DisplayName("creates an assessment from a specification extracted from the assessed value")
		void createsAnAssessmentFromASpecificationExtractedFromTheAssessedValue()
		{
			record Candidate(String subject, int minLength)
			{
			}

			var assessment = AssessmentFactory.fromExtractor(Candidate::subject,
					candidate -> value -> value.length() >= candidate.minLength(),
					() -> TOO_SHORT);

			var result = assessment.assess(new Candidate("ab", 3));

			assertThat(result.violations())
					.as("violations")
					.containsExactly(TOO_SHORT);
		}
	}
}
