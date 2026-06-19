package de.gupta.validation.aegis.api.assessment.assessment.decision;

import de.gupta.validation.aegis.api.assessment.assessment.policy.DecisionPolicy;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Decision")
final class DecisionTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);

	private enum OperationalDecision
	{
		ALLOW,
		QUARANTINE
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for decision()")
	final class ForDecisionFactory
	{
		@Test
		@DisplayName("retains the value, evidence, and decision")
		void retainsTheValueEvidenceAndDecision()
		{
			var assessmentResult = AssessmentResultFactory.with(LOW);
			var decision = DecisionFactory.decision("operation", assessmentResult, OperationalDecision.QUARANTINE);

			assertThat(decision.value())
					.as("value")
					.isEqualTo("operation");
			assertThat(decision.assessmentResult())
					.as("assessment result")
					.isSameAs(assessmentResult);
			assertThat(decision.decision())
					.as("decision")
					.isEqualTo(OperationalDecision.QUARANTINE);
		}

		@Test
		@DisplayName("rejects null values to keep decisions materialized")
		void rejectsNullValuesToKeepDecisionsMaterialized()
		{
			var assessmentResult = AssessmentResultFactory.empty();

			assertThatThrownBy(() -> DecisionFactory.decision(null, assessmentResult, OperationalDecision.ALLOW))
					.as("decision with null value")
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContainingAll("Value", "null");
		}
	}

	@Nested
	@DisplayName("for decided()")
	final class ForDecided
	{
		@Test
		@DisplayName("derives the decision from the supplied policy")
		void derivesTheDecisionFromTheSuppliedPolicy()
		{
			var assessmentResult = AssessmentResultFactory.with(LOW);
			DecisionPolicy<OperationalDecision> policy = result -> result.highestSeverityLessThan(Severity.HIGH)
					? OperationalDecision.ALLOW
					: OperationalDecision.QUARANTINE;

			var decision = DecisionFactory.decided("operation", assessmentResult, policy);

			assertThat(decision.decision())
					.as("decision")
					.isEqualTo(OperationalDecision.ALLOW);
		}
	}

	@Nested
	@DisplayName("for map()")
	final class ForMap
	{
		@Test
		@DisplayName("maps the value while preserving the assessment result and decision")
		void mapsTheValueWhilePreservingTheAssessmentResultAndDecision()
		{
			var assessmentResult = AssessmentResultFactory.with(LOW);
			var decision = DecisionFactory.decision("operation", assessmentResult, OperationalDecision.QUARANTINE);

			var mapped = decision.map(String::length);

			assertThat(mapped.value())
					.as("mapped value")
					.isEqualTo(9);
			assertThat(mapped.assessmentResult())
					.as("assessment result")
					.isSameAs(assessmentResult);
			assertThat(mapped.decision())
					.as("decision")
					.isEqualTo(OperationalDecision.QUARANTINE);
		}
	}
}
