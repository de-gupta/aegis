package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import de.gupta.validation.aegis.api.validation.validation.factories.ComparisonValidations;
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

@DisplayName("ComparisonValidations")
final class ComparisonValidationsTest
{
	private static final TestViolation VIOLATION = new TestViolation("comparison failed", Severity.HIGH);

	private record Sample(int value, int threshold)
	{
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for comparisonSpecification()")
	final class ForComparisonSpecification
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("comparesTheExtractedValueAgainstTheExtractedThresholdCases")
		@DisplayName("compares the extracted value against the extracted threshold")
		void comparesTheExtractedValueAgainstTheExtractedThreshold(final String as, final Sample sample,
		                                                           final ComparisonType comparisonType,
		                                                           final Set<Violation> expectedViolations)
		{
			var validation = ComparisonValidations.comparisonSpecification(
					Sample::value,
					Sample::threshold,
					comparisonType,
					() -> VIOLATION);

			var result = validation.validate(sample);

			assertThat(result.violations())
					.as("violations for %s", as)
					.isEqualTo(expectedViolations);
		}

		private static Stream<Arguments> comparesTheExtractedValueAgainstTheExtractedThresholdCases()
		{
			return Stream.of(
					Arguments.of("less-than accepts smaller values", new Sample(4, 5), ComparisonType.LESS_THAN,
							Set.of()),
					Arguments.of("less-than rejects equal values", new Sample(5, 5), ComparisonType.LESS_THAN,
							Set.of(VIOLATION)),
					Arguments.of("greater-than-or-equal accepts equal values", new Sample(5, 5),
							ComparisonType.GREATER_THAN_OR_EQUAL, Set.of())
			);
		}
	}
}