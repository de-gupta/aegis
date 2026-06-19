package de.gupta.validation.aegis.api.validation.policy;

import de.gupta.commons.utility.comparison.ComparisonType;
import de.gupta.validation.aegis.api.validation.validation.policy.ValidationPolicyFactory;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResultFactory;
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

@DisplayName("ValidationPolicyFactory")
final class ValidationPolicyFactoryTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);
	private static final TestViolation HIGH = new TestViolation("high", Severity.HIGH);
	private static final TestViolation CRITICAL = new TestViolation("critical", Severity.CRITICAL);

	private static ValidationResult resultWith(final Violation... violations)
	{
		return ValidationResultFactory.with(Set.of(violations));
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for withComparingSeverity(Severity, ComparisonType)")
	final class ForWithComparingSeverity
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("createsAPolicyUsingTheGivenComparisonTypeCases")
		@DisplayName("creates a policy using the given comparison type")
		void createsAPolicyUsingTheGivenComparisonType(final String as, final ComparingSeverityCase tc)
		{
			var policy = ValidationPolicyFactory.withComparingSeverity(tc.threshold(), tc.comparisonType());

			assertThat(policy.isValid(tc.validationResult()))
					.as("%s", as)
					.isEqualTo(tc.expected());
		}

		private static Stream<Arguments> createsAPolicyUsingTheGivenComparisonTypeCases()
		{
			return Stream.of(
					ComparingSeverityCase.of("less-than policy accepts lower severities",
							Severity.HIGH, ComparisonType.LESS_THAN, resultWith(LOW), true),
					ComparingSeverityCase.of("less-than policy rejects equal severities",
							Severity.HIGH, ComparisonType.LESS_THAN, resultWith(HIGH), false),
					ComparingSeverityCase.of("equal policy accepts equal severities",
							Severity.HIGH, ComparisonType.EQUAL, resultWith(HIGH), true),
					ComparingSeverityCase.of("greater-than policy accepts higher severities",
							Severity.HIGH, ComparisonType.GREATER_THAN, resultWith(CRITICAL), true)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record ComparingSeverityCase(String as, Severity threshold, ComparisonType comparisonType,
		                                     ValidationResult validationResult, boolean expected)
		{
			private static ComparingSeverityCase of(final String as, final Severity threshold,
			                                        final ComparisonType comparisonType,
			                                        final ValidationResult validationResult, final boolean expected)
			{
				return new ComparingSeverityCase(as, threshold, comparisonType, validationResult, expected);
			}
		}
	}

	@Nested
	@DisplayName("for withSeverityLessThan(Severity)")
	final class ForWithSeverityLessThan
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("acceptsOnlyStrictlyLowerSeveritiesCases")
		@DisplayName("accepts only strictly lower severities")
		void acceptsOnlyStrictlyLowerSeverities(final String as, final Severity threshold,
		                                        final ValidationResult validationResult, final boolean expected)
		{
			var policy = ValidationPolicyFactory.withSeverityLessThan(threshold);

			assertThat(policy.isValid(validationResult))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> acceptsOnlyStrictlyLowerSeveritiesCases()
		{
			return Stream.of(
					Arguments.of("strict policy accepts lower severity", Severity.HIGH, resultWith(LOW), true),
					Arguments.of("strict policy rejects equal severity", Severity.HIGH, resultWith(HIGH), false),
					Arguments.of("strict policy rejects higher severity", Severity.HIGH, resultWith(CRITICAL), false)
			);
		}
	}

	@Nested
	@DisplayName("for withSeverityAtMost(Severity)")
	final class ForWithSeverityAtMost
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("acceptsLowerAndEqualSeveritiesCases")
		@DisplayName("accepts lower and equal severities")
		void acceptsLowerAndEqualSeverities(final String as, final Severity threshold,
		                                    final ValidationResult validationResult, final boolean expected)
		{
			var policy = ValidationPolicyFactory.withSeverityAtMost(threshold);

			assertThat(policy.isValid(validationResult))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> acceptsLowerAndEqualSeveritiesCases()
		{
			return Stream.of(
					Arguments.of("at-most policy accepts lower severity", Severity.HIGH, resultWith(LOW), true),
					Arguments.of("at-most policy accepts equal severity", Severity.HIGH, resultWith(HIGH), true),
					Arguments.of("at-most policy rejects higher severity", Severity.HIGH, resultWith(CRITICAL), false)
			);
		}
	}
}