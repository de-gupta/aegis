package de.gupta.validation.aegis.api.assessment.assessment.result;

import de.gupta.commons.utility.comparison.ComparisonType;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AssessmentResult")
final class AssessmentResultTest
{
	private static final TestViolation CONSIDERATION = new TestViolation("consideration", Severity.CONSIDERATION);
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);
	private static final TestViolation HIGH = new TestViolation("high", Severity.HIGH);
	private static final TestViolation CRITICAL = new TestViolation("critical", Severity.CRITICAL);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for highestSeverity()")
	final class ForHighestSeverity
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("returnsTheHighestSeverityCases")
		@DisplayName("returns the highest violation severity when violations are present")
		void returnsTheHighestViolationSeverityWhenViolationsArePresent(final String as,
		                                                                final HighestSeverityCase tc)
		{
			var result = AssessmentResultFactory.with(tc.violations());

			assertThat(result.highestSeverity())
					.as("%s", as)
					.isEqualTo(Optional.of(tc.expected()));
		}

		private static Stream<Arguments> returnsTheHighestSeverityCases()
		{
			return Stream.of(
					HighestSeverityCase.of("single violation keeps its own severity", Set.of(LOW), Severity.LOW),
					HighestSeverityCase.of("higher severity dominates lower severity",
							Set.of(CONSIDERATION, HIGH, LOW), Severity.HIGH),
					HighestSeverityCase.of("critical dominates every other severity",
							Set.of(LOW, HIGH, CRITICAL), Severity.CRITICAL)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record HighestSeverityCase(String as, Set<Violation> violations, Severity expected)
		{
			private static HighestSeverityCase of(final String as, final Set<Violation> violations,
			                                      final Severity expected)
			{
				return new HighestSeverityCase(as, violations, expected);
			}
		}
	}

	@Nested
	@DisplayName("when there are no violations")
	final class WhenThereAreNoViolations
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("matchesUnderEveryThresholdCases")
		@DisplayName("matches every threshold comparison")
		void matchesEveryThresholdComparison(final String as, final Severity threshold)
		{
			var result = AssessmentResultFactory.empty();

			assertThat(result)
					.as("%s", as)
					.satisfies(assessmentResult ->
					{
						assertThat(assessmentResult.violations())
								.as("violations for %s", as)
								.isEmpty();
						assertThat(assessmentResult.highestSeverity())
								.as("highest severity for %s", as)
								.isEmpty();
						assertThat(assessmentResult.highestSeverityLessThan(threshold))
								.as("comparison for %s", as)
								.isEqualTo(true);
					});
		}

		private static Stream<Arguments> matchesUnderEveryThresholdCases()
		{
			return Stream.of(
					Arguments.of("consideration threshold treats empty result as matching", Severity.CONSIDERATION),
					Arguments.of("medium threshold treats empty result as matching", Severity.MEDIUM),
					Arguments.of("critical threshold treats empty result as matching", Severity.CRITICAL)
			);
		}
	}

	@Nested
	@DisplayName("for add()")
	final class ForAdd
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("unionsViolationsCases")
		@DisplayName("unions violations and recalculates the highest severity from the combined set")
		void unionsViolationsAndRecalculatesTheHighestSeverityFromTheCombinedSet(final String as,
		                                                                         final AddCase tc)
		{
			var left = AssessmentResultFactory.with(tc.left());
			var right = AssessmentResultFactory.with(tc.right());

			var result = left.add(right);

			assertThat(result)
					.as("%s", as)
					.satisfies(assessmentResult ->
					{
						assertThat(assessmentResult.violations())
								.as("violations for %s", as)
								.isEqualTo(tc.expectedViolations());
						assertThat(assessmentResult.highestSeverity())
								.as("highest severity for %s", as)
								.isEqualTo(tc.expectedHighestSeverity());
					});
		}

		private static Stream<Arguments> unionsViolationsCases()
		{
			return Stream.of(
					AddCase.of("distinct violations from both sides are preserved",
							Set.of(LOW),
							Set.of(HIGH),
							Set.of(LOW, HIGH),
							Optional.of(Severity.HIGH)),
					AddCase.of("duplicate violations collapse to one entry through set union",
							Set.of(LOW, HIGH),
							Set.of(HIGH),
							Set.of(LOW, HIGH),
							Optional.of(Severity.HIGH)),
					AddCase.of("adding two empty results remains empty",
							Set.of(),
							Set.of(),
							Set.of(),
							Optional.empty())
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record AddCase(String as, Set<Violation> left, Set<Violation> right, Set<Violation> expectedViolations,
		                       Optional<Severity> expectedHighestSeverity)
		{
			private static AddCase of(final String as, final Set<Violation> left, final Set<Violation> right,
			                          final Set<Violation> expectedViolations,
			                          final Optional<Severity> expectedHighestSeverity)
			{
				return new AddCase(as, left, right, expectedViolations, expectedHighestSeverity);
			}
		}
	}

	@Nested
	@DisplayName("for highestSeverityMatches(Severity, ComparisonType)")
	final class ForHighestSeverityMatches
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("matchesCases")
		@DisplayName("compares the highest severity using the supplied comparison type")
		void comparesTheHighestSeverityUsingTheSuppliedComparisonType(final String as,
		                                                              final MatchesCase tc)
		{
			var result = AssessmentResultFactory.with(tc.violations());

			assertThat(result)
					.as("%s", as)
					.satisfies(assessmentResult ->
							assertThat(assessmentResult.highestSeverityMatches(tc.threshold(), tc.comparisonType()))
									.as("comparison for %s", as)
									.isEqualTo(tc.expected()));
		}

		private static Stream<Arguments> matchesCases()
		{
			return Stream.of(
					MatchesCase.of("less-than accepts strictly lower severity",
							Set.of(LOW), Severity.HIGH, ComparisonType.LESS_THAN, true),
					MatchesCase.of("less-than rejects equal severity",
							Set.of(HIGH), Severity.HIGH, ComparisonType.LESS_THAN, false),
					MatchesCase.of("less-than-or-equal accepts equal severity",
							Set.of(HIGH), Severity.HIGH, ComparisonType.LESS_THAN_OR_EQUAL, true),
					MatchesCase.of("greater-than accepts strictly higher severity",
							Set.of(CRITICAL), Severity.HIGH, ComparisonType.GREATER_THAN, true),
					MatchesCase.of("equal rejects different severity",
							Set.of(LOW), Severity.HIGH, ComparisonType.EQUAL, false),
					MatchesCase.of("empty assessment result stays matching for every comparison type",
							Set.of(), Severity.HIGH, ComparisonType.GREATER_THAN, true)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record MatchesCase(String as, Set<Violation> violations, Severity threshold,
		                           ComparisonType comparisonType, boolean expected)
		{
			private static MatchesCase of(final String as, final Set<Violation> violations,
			                              final Severity threshold, final ComparisonType comparisonType,
			                              final boolean expected)
			{
				return new MatchesCase(as, violations, threshold, comparisonType, expected);
			}
		}
	}
}
