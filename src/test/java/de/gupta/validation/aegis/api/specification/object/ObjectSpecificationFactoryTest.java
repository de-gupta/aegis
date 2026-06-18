package de.gupta.validation.aegis.api.specification.object;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ObjectSpecificationFactory")
final class ObjectSpecificationFactoryTest
{
	@Nested
	@DisplayName("for notNull()")
	final class ForNotNull
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("checksWhetherTheCandidateIsPresentCases")
		@DisplayName("checks whether the candidate is present")
		void checksWhetherTheCandidateIsPresent(final String as, final Object candidate, final boolean expected)
		{
			var specification = ObjectSpecificationFactory.notNull();

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> checksWhetherTheCandidateIsPresentCases()
		{
			return Stream.of(
					Arguments.of("null candidates are rejected", null, false),
					Arguments.of("non-null candidates are accepted", "value", true)
			);
		}
	}

	@Nested
	@DisplayName("for consistent()")
	final class ForConsistent
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("delegatesToTheSuppliedPredicateCases")
		@DisplayName("delegates to the supplied predicate")
		void delegatesToTheSuppliedPredicate(final String as, final String candidate, final boolean expected)
		{
			var specification = ObjectSpecificationFactory.consistent((String value) -> value.length() >= 3);

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> delegatesToTheSuppliedPredicateCases()
		{
			return Stream.of(
					Arguments.of("predicate-accepted candidates are accepted", "trimmed", true),
					Arguments.of("predicate-rejected candidates are rejected", "no", false)
			);
		}
	}
}
