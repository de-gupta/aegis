package de.gupta.validation.aegis.api.specification.time;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OffsetDateTimeSpecificationFactory")
final class OffsetDateTimeSpecificationFactoryTest
{
	private static final OffsetDateTime THRESHOLD = OffsetDateTime.of(2026, 6, 17, 12, 0, 0, 0, ZoneOffset.UTC);

	@FunctionalInterface
	private interface OffsetDateTimeSpecificationFactoryInvocation
	{
		Specification<OffsetDateTime> create(OffsetDateTime threshold);
	}

	@Nested
	@DisplayName("for direct comparison factories")
	final class ForDirectComparisonFactories
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("evaluatesTheCandidateAgainstTheThresholdCases")
		@DisplayName("evaluates the candidate against the threshold")
		void evaluatesTheCandidateAgainstTheThreshold(final String as,
		                                              final OffsetDateTimeSpecificationFactoryInvocation invocation,
		                                              final OffsetDateTime candidate, final boolean expected)
		{
			var specification = invocation.create(THRESHOLD);

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> evaluatesTheCandidateAgainstTheThresholdCases()
		{
			return Stream.of(
					Arguments.of("isBefore accepts earlier instants",
							(OffsetDateTimeSpecificationFactoryInvocation) OffsetDateTimeSpecificationFactory::isBefore,
							THRESHOLD.minusMinutes(1), true),
					Arguments.of("isNotAfter accepts the threshold itself",
							(OffsetDateTimeSpecificationFactoryInvocation) OffsetDateTimeSpecificationFactory::isNotAfter,
							THRESHOLD, true),
					Arguments.of("isAtTheSameTime accepts equal instants",
							(OffsetDateTimeSpecificationFactoryInvocation) OffsetDateTimeSpecificationFactory::isAtTheSameTime,
							THRESHOLD, true),
					Arguments.of("isNotBefore accepts later instants",
							(OffsetDateTimeSpecificationFactoryInvocation) OffsetDateTimeSpecificationFactory::isNotBefore,
							THRESHOLD.plusMinutes(1), true),
					Arguments.of("isAfter rejects the threshold itself",
							(OffsetDateTimeSpecificationFactoryInvocation) OffsetDateTimeSpecificationFactory::isAfter,
							THRESHOLD, false)
			);
		}
	}

	@Nested
	@DisplayName("for compare()")
	final class ForCompare
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("delegatesToTheRequestedComparisonTypeCases")
		@DisplayName("delegates to the requested comparison type")
		void delegatesToTheRequestedComparisonType(final String as, final ComparisonType comparisonType,
		                                           final OffsetDateTime candidate, final boolean expected)
		{
			var specification = OffsetDateTimeSpecificationFactory.compare(THRESHOLD, comparisonType);

			assertThat(specification.isSatisfiedBy(candidate))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> delegatesToTheRequestedComparisonTypeCases()
		{
			return Stream.of(
					Arguments.of("less-than accepts an earlier instant", ComparisonType.LESS_THAN,
							THRESHOLD.minusMinutes(1), true),
					Arguments.of("equal accepts the threshold instant", ComparisonType.EQUAL, THRESHOLD, true),
					Arguments.of("greater-than rejects the threshold instant", ComparisonType.GREATER_THAN, THRESHOLD,
							false)
			);
		}
	}
}