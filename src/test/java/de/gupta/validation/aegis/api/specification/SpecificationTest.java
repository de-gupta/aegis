package de.gupta.validation.aegis.api.specification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Specification")
final class SpecificationTest
{
	private final Specification<Integer> even = value -> value % 2 == 0;
	private final Specification<Integer> greaterThanTen = value -> value > 10;

	@Nested
	@DisplayName("for meet()")
	final class ForMeet
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("appliesLogicalConjunctionCases")
		@DisplayName("applies logical conjunction to both specifications")
		void appliesLogicalConjunctionToBothSpecifications(final String as, final int candidate,
		                                                   final boolean expected)
		{
			var result = even.meet(greaterThanTen);

			assertThat(result.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> appliesLogicalConjunctionCases()
		{
			return Stream.of(
					Arguments.of("value satisfying both specifications returns true", 12, true),
					Arguments.of("value satisfying only one specification returns false", 8, false),
					Arguments.of("value satisfying neither specification returns false", 7, false)
			);
		}
	}

	@Nested
	@DisplayName("for join()")
	final class ForJoin
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("appliesLogicalDisjunctionCases")
		@DisplayName("applies logical disjunction to both specifications")
		void appliesLogicalDisjunctionToBothSpecifications(final String as, final int candidate,
		                                                   final boolean expected)
		{
			var result = even.join(greaterThanTen);

			assertThat(result.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> appliesLogicalDisjunctionCases()
		{
			return Stream.of(
					Arguments.of("value satisfying both specifications returns true", 12, true),
					Arguments.of("value satisfying only the left specification returns true", 8, true),
					Arguments.of("value satisfying only the right specification returns true", 11, true),
					Arguments.of("value satisfying neither specification returns false", 7, false)
			);
		}
	}

	@Nested
	@DisplayName("for complement()")
	final class ForComplement
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("negatesTheUnderlyingSpecificationCases")
		@DisplayName("negates the underlying specification")
		void negatesTheUnderlyingSpecification(final String as, final int candidate, final boolean expected)
		{
			var result = even.complement();

			assertThat(result.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> negatesTheUnderlyingSpecificationCases()
		{
			return Stream.of(
					Arguments.of("candidate accepted by the original specification becomes rejected", 12, false),
					Arguments.of("candidate rejected by the original specification becomes accepted", 7, true)
			);
		}
	}

	@Nested
	@DisplayName("for supremum()")
	final class ForSupremum
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("acceptsEveryCandidateCases")
		@DisplayName("accepts every candidate")
		void acceptsEveryCandidate(final String as, final int candidate)
		{
			var result = even.supremum();

			assertThat(result.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(true);
		}

		private static Stream<Arguments> acceptsEveryCandidateCases()
		{
			return Stream.of(
					Arguments.of("positive values are accepted", 12),
					Arguments.of("negative values are accepted", -5)
			);
		}
	}

	@Nested
	@DisplayName("for infimum()")
	final class ForInfimum
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("rejectsEveryCandidateCases")
		@DisplayName("rejects every candidate")
		void rejectsEveryCandidate(final String as, final int candidate)
		{
			var result = even.infimum();

			assertThat(result.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(false);
		}

		private static Stream<Arguments> rejectsEveryCandidateCases()
		{
			return Stream.of(
					Arguments.of("positive values are rejected", 12),
					Arguments.of("negative values are rejected", -5)
			);
		}
	}
}
