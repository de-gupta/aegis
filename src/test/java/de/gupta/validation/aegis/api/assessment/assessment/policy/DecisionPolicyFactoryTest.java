package de.gupta.validation.aegis.api.assessment.assessment.policy;

import de.gupta.commons.utility.comparison.ComparisonType;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DecisionPolicyFactory")
final class DecisionPolicyFactoryTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);
	private static final TestViolation HIGH = new TestViolation("high", Severity.HIGH);

	private static AssessmentResult resultWith(final Violation... violations)
	{
		return AssessmentResultFactory.with(Set.of(violations));
	}

	private enum OperationalDecision
	{
		ALLOW,
		QUARANTINE,
		REJECT
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for withComparingSeverity()")
	final class ForWithComparingSeverity
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("returnsConfiguredDecisionCases")
		@DisplayName("returns the configured decision based on the severity comparison")
		void returnsTheConfiguredDecisionBasedOnTheSeverityComparison(final String as, final DecisionCase tc)
		{
			var policy = DecisionPolicyFactory.withComparingSeverity(tc.threshold(), tc.comparisonType(),
					tc.matchingDecision(), tc.nonMatchingDecision());

			assertThat(policy.decide(tc.assessmentResult()))
					.as("%s", as)
					.isEqualTo(tc.expected());
		}

		private static Stream<Arguments> returnsConfiguredDecisionCases()
		{
			return Stream.of(
					DecisionCase.of("strictly lower severity takes the matching decision",
							Severity.HIGH, ComparisonType.LESS_THAN,
							resultWith(LOW), OperationalDecision.ALLOW, OperationalDecision.REJECT,
							OperationalDecision.ALLOW),
					DecisionCase.of("equal severity takes the non-matching decision for strict comparisons",
							Severity.HIGH, ComparisonType.LESS_THAN,
							resultWith(HIGH), OperationalDecision.ALLOW, OperationalDecision.REJECT,
							OperationalDecision.REJECT),
					DecisionCase.of("empty result still counts as matching",
							Severity.HIGH, ComparisonType.GREATER_THAN,
							AssessmentResultFactory.empty(), OperationalDecision.QUARANTINE,
							OperationalDecision.REJECT, OperationalDecision.QUARANTINE)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record DecisionCase(String as, Severity threshold, ComparisonType comparisonType,
		                            AssessmentResult assessmentResult, OperationalDecision matchingDecision,
		                            OperationalDecision nonMatchingDecision, OperationalDecision expected)
		{
			private static DecisionCase of(final String as, final Severity threshold,
			                               final ComparisonType comparisonType,
			                               final AssessmentResult assessmentResult,
			                               final OperationalDecision matchingDecision,
			                               final OperationalDecision nonMatchingDecision,
			                               final OperationalDecision expected)
			{
				return new DecisionCase(as, threshold, comparisonType, assessmentResult, matchingDecision,
						nonMatchingDecision, expected);
			}
		}
	}

	@Nested
	@DisplayName("for constant()")
	final class ForConstant
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("alwaysReturnsConfiguredDecisionCases")
		@DisplayName("always returns the configured decision")
		void alwaysReturnsTheConfiguredDecision(final String as, final AssessmentResult assessmentResult)
		{
			var policy = DecisionPolicyFactory.constant(OperationalDecision.QUARANTINE);

			assertThat(policy.decide(assessmentResult))
					.as("%s", as)
					.isEqualTo(OperationalDecision.QUARANTINE);
		}

		private static Stream<Arguments> alwaysReturnsConfiguredDecisionCases()
		{
			return Stream.of(
					Arguments.of("empty result", AssessmentResultFactory.empty()),
					Arguments.of("result with violations", resultWith(LOW, HIGH))
			);
		}
	}
}