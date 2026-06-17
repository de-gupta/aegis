package de.gupta.validation.aegis.api.specification.comparison;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ExtractorComparisonSpecificationFactory")
final class ExtractorComparisonSpecificationFactoryTest
{
	private record Measurement(int value, int threshold)
	{
	}

	@Nested
	@DisplayName("for comparisonSpecification()")
	final class ForComparisonSpecification
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("comparesTheExtractedValueAgainstTheExtractedThresholdCases")
		@DisplayName("compares the extracted value against the extracted threshold")
		void comparesTheExtractedValueAgainstTheExtractedThreshold(final String as, final Measurement measurement,
		                                                           final ComparisonType comparisonType,
		                                                           final boolean expected)
		{
			var specification = ExtractorComparisonSpecificationFactory.comparisonSpecification(
					Measurement::value,
					Measurement::threshold,
					Comparator.naturalOrder(),
					comparisonType);

			assertThat(specification.isSatisfiedBy(measurement))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> comparesTheExtractedValueAgainstTheExtractedThresholdCases()
		{
			return Stream.of(
					Arguments.of("less-than accepts smaller extracted values", new Measurement(4, 5),
							ComparisonType.LESS_THAN, true),
					Arguments.of("greater-than-or-equal accepts equal extracted values", new Measurement(5, 5),
							ComparisonType.GREATER_THAN_OR_EQUAL, true),
					Arguments.of("greater-than rejects smaller extracted values", new Measurement(4, 5),
							ComparisonType.GREATER_THAN, false)
			);
		}
	}
}