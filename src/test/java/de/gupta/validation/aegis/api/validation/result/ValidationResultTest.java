package de.gupta.validation.aegis.api.validation.result;

import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import de.gupta.commons.utility.comparison.ComparisonType;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationResult")
final class ValidationResultTest
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
			var result = ValidationResultFactory.with(tc.violations());

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
		@MethodSource("isValidUnderEveryThresholdCases")
		@DisplayName("is valid under every threshold")
		void isValidUnderEveryThreshold(final String as, final Severity threshold)
		{
			var result = ValidationResultFactory.empty();

			assertThat(result)
					.as("%s", as)
					.satisfies(validationResult ->
					{
						assertThat(validationResult.violations())
								.as("violations for %s", as)
								.isEmpty();
						assertThat(validationResult.highestSeverity())
								.as("highest severity for %s", as)
								.isEmpty();
						assertThat(validationResult.isLessThan(threshold))
								.as("validity for %s", as)
								.isEqualTo(true);
					});
		}

		private static Stream<Arguments> isValidUnderEveryThresholdCases()
		{
			return Stream.of(
					Arguments.of("consideration threshold treats empty result as valid", Severity.CONSIDERATION),
					Arguments.of("medium threshold treats empty result as valid", Severity.MEDIUM),
					Arguments.of("critical threshold treats empty result as valid", Severity.CRITICAL)
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
			var left = ValidationResultFactory.with(tc.left());
			var right = ValidationResultFactory.with(tc.right());

			var result = left.add(right);

			assertThat(result)
					.as("%s", as)
					.satisfies(validationResult ->
					{
						assertThat(validationResult.violations())
								.as("violations for %s", as)
								.isEqualTo(tc.expectedViolations());
						assertThat(validationResult.highestSeverity())
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
	@DisplayName("for isValid(Severity)")
	final class ForIsValid
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("isValidCases")
		@DisplayName("compares the highest severity against the threshold")
		void comparesTheHighestSeverityAgainstTheThreshold(final String as, final IsValidCase tc)
		{
			var result = ValidationResultFactory.with(tc.violations());

			assertThat(result)
					.as("%s", as)
					.satisfies(validationResult ->
							assertThat(validationResult.isLessThan(tc.threshold()))
									.as("validity for %s", as)
									.isEqualTo(tc.expected()));
		}

		private static Stream<Arguments> isValidCases()
		{
			return Stream.of(
					IsValidCase.of("lower severity than the threshold counts as valid",
							Set.of(LOW), Severity.HIGH, true),
					IsValidCase.of("equal severity to the threshold counts as invalid",
							Set.of(HIGH), Severity.HIGH, false),
					IsValidCase.of("higher severity than the threshold counts as invalid",
							Set.of(CRITICAL), Severity.HIGH, false)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record IsValidCase(String as, Set<Violation> violations, Severity threshold, boolean expected)
		{
			private static IsValidCase of(final String as, final Set<Violation> violations,
			                              final Severity threshold, final boolean expected)
			{
				return new IsValidCase(as, violations, threshold, expected);
			}
		}
	}

	@Nested
	@DisplayName("for isValid(Severity, ComparisonType)")
	final class ForIsValidWithComparisonType
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("comparesTheHighestSeverityUsingTheSuppliedComparisonTypeCases")
		@DisplayName("compares the highest severity using the supplied comparison type")
		void comparesTheHighestSeverityUsingTheSuppliedComparisonType(final String as,
		                                                             final IsValidWithComparisonTypeCase tc)
		{
			var result = ValidationResultFactory.with(tc.violations());

			assertThat(result)
					.as("%s", as)
					.satisfies(validationResult ->
							assertThat(validationResult.isValid(tc.threshold(), tc.comparisonType()))
									.as("validity for %s", as)
									.isEqualTo(tc.expected()));
		}

		private static Stream<Arguments> comparesTheHighestSeverityUsingTheSuppliedComparisonTypeCases()
		{
			return Stream.of(
					IsValidWithComparisonTypeCase.of("less-than accepts strictly lower severity",
							Set.of(LOW), Severity.HIGH, ComparisonType.LESS_THAN, true),
					IsValidWithComparisonTypeCase.of("less-than rejects equal severity",
							Set.of(HIGH), Severity.HIGH, ComparisonType.LESS_THAN, false),
					IsValidWithComparisonTypeCase.of("less-than-or-equal accepts equal severity",
							Set.of(HIGH), Severity.HIGH, ComparisonType.LESS_THAN_OR_EQUAL, true),
					IsValidWithComparisonTypeCase.of("greater-than accepts strictly higher severity",
							Set.of(CRITICAL), Severity.HIGH, ComparisonType.GREATER_THAN, true),
					IsValidWithComparisonTypeCase.of("greater-than rejects lower severity",
							Set.of(LOW), Severity.HIGH, ComparisonType.GREATER_THAN, false),
					IsValidWithComparisonTypeCase.of("equal accepts matching severity",
							Set.of(HIGH), Severity.HIGH, ComparisonType.EQUAL, true),
					IsValidWithComparisonTypeCase.of("equal rejects different severity",
							Set.of(LOW), Severity.HIGH, ComparisonType.EQUAL, false),
					IsValidWithComparisonTypeCase.of("empty validation result stays valid for every comparison type",
							Set.of(), Severity.HIGH, ComparisonType.GREATER_THAN, true)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record IsValidWithComparisonTypeCase(String as, Set<Violation> violations, Severity threshold,
		                                             ComparisonType comparisonType, boolean expected)
		{
			private static IsValidWithComparisonTypeCase of(final String as, final Set<Violation> violations,
			                                                final Severity threshold,
			                                                final ComparisonType comparisonType,
			                                                final boolean expected)
			{
				return new IsValidWithComparisonTypeCase(as, violations, threshold, comparisonType, expected);
			}
		}
	}
}
