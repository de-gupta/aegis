package de.gupta.validation.aegis.api.specification.number;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DoubleBasedPotentiallyImpreciseNumberSpecificationFactory")
final class DoubleBasedPotentiallyImpreciseNumberSpecificationFactoryTest
{
	@FunctionalInterface
	private interface ThresholdSpecificationFactoryInvocation
	{
		Specification<Double> create(Number threshold);
	}

	@FunctionalInterface
	private interface SignSpecificationFactoryInvocation
	{
		Specification<Double> create();
	}

	@Nested
	@DisplayName("for threshold-based factories")
	final class ForThresholdBasedFactories
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("evaluatesTheCandidateAgainstTheThresholdCases")
		@DisplayName("evaluates the candidate against the threshold")
		void evaluatesTheCandidateAgainstTheThreshold(final String as,
		                                              final ThresholdSpecificationFactoryInvocation invocation,
		                                              final double candidate, final boolean expected)
		{
			var specification = invocation.create(10);

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> evaluatesTheCandidateAgainstTheThresholdCases()
		{
			return Stream.of(
					Arguments.of("lessThanOrEqualTo accepts the threshold value",
							(ThresholdSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::lessThanOrEqualTo,
							10.0d, true),
					Arguments.of("equal accepts an equal value",
							(ThresholdSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::equal,
							10.0d, true),
					Arguments.of("greaterThanOrEqualTo accepts the threshold value",
							(ThresholdSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::greaterThanOrEqualTo,
							10.0d, true),
					Arguments.of("notEqual accepts a different value",
							(ThresholdSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::notEqual,
							11.0d, true),
					Arguments.of("lessThan accepts a smaller value",
							(ThresholdSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::lessThan,
							9.0d, true),
					Arguments.of("greaterThan accepts a larger value",
							(ThresholdSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::greaterThan,
							11.0d, true)
			);
		}
	}

	@Nested
	@DisplayName("for sign-based factories")
	final class ForSignBasedFactories
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("evaluatesTheCandidateRelativeToZeroCases")
		@DisplayName("evaluates the candidate relative to zero")
		void evaluatesTheCandidateRelativeToZero(final String as, final SignSpecificationFactoryInvocation invocation,
		                                         final double candidate, final boolean expected)
		{
			var specification = invocation.create();

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> evaluatesTheCandidateRelativeToZeroCases()
		{
			return Stream.of(
					Arguments.of("negative accepts numbers below zero",
							(SignSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::negative,
							-0.1d, true),
					Arguments.of("nonNegative accepts zero",
							(SignSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::nonNegative,
							0.0d, true),
					Arguments.of("positive accepts numbers above zero",
							(SignSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::positive,
							0.1d, true),
					Arguments.of("nonPositive accepts zero",
							(SignSpecificationFactoryInvocation) DoubleBasedPotentiallyImpreciseNumberSpecificationFactory::nonPositive,
							0.0d, true)
			);
		}
	}

	@Nested
	@DisplayName("for comparison()")
	final class ForComparison
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("delegatesToTheRequestedComparisonTypeCases")
		@DisplayName("delegates to the requested comparison type")
		void delegatesToTheRequestedComparisonType(final String as, final ComparisonType comparisonType,
		                                           final double candidate, final boolean expected)
		{
			var specification =
					DoubleBasedPotentiallyImpreciseNumberSpecificationFactory.comparison(10, comparisonType);

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> delegatesToTheRequestedComparisonTypeCases()
		{
			return Stream.of(
					Arguments.of("less-than accepts a smaller candidate", ComparisonType.LESS_THAN, 9.0d, true),
					Arguments.of("greater-than-or-equal accepts the threshold value",
							ComparisonType.GREATER_THAN_OR_EQUAL, 10.0d, true)
			);
		}
	}
}