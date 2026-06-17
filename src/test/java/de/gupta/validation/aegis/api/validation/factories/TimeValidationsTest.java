package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.specification.comparison.ComparisonType;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TimeValidations")
final class TimeValidationsTest
{
	private static final TestViolation VIOLATION = new TestViolation("time comparison failed", Severity.HIGH);

	@FunctionalInterface
	private interface ValidationFactoryInvocation
	{
		Validation<Window> create(Supplier<TestViolation> violationSupplier);
	}

	private record Window(OffsetDateTime value, OffsetDateTime threshold)
	{
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for direct time comparisons")
	final class ForDirectTimeComparisons
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("comparesTheExtractedValueAgainstTheExtractedThresholdCases")
		@DisplayName("compares the extracted value against the extracted threshold")
		void comparesTheExtractedValueAgainstTheExtractedThreshold(final String as,
		                                                           final ValidationFactoryInvocation invocation,
		                                                           final Window window,
		                                                           final Set<Violation> expectedViolations)
		{
			var validation = invocation.create(() -> VIOLATION);

			var result = validation.validate(window);

			assertThat(result.violations())
					.as("violations for %s", as)
					.isEqualTo(expectedViolations);
		}

		private static Stream<Arguments> comparesTheExtractedValueAgainstTheExtractedThresholdCases()
		{
			var threshold = OffsetDateTime.of(2026, 6, 17, 12, 0, 0, 0, ZoneOffset.UTC);

			return Stream.of(
					Arguments.of("isBefore accepts earlier instants",
							(ValidationFactoryInvocation) violationSupplier -> TimeValidations.isBefore(
									Window::value, Window::threshold, violationSupplier),
							new Window(threshold.minusMinutes(1), threshold), Set.of()),
					Arguments.of("isAfter rejects equal instants",
							(ValidationFactoryInvocation) violationSupplier -> TimeValidations.isAfter(
									Window::value, Window::threshold, violationSupplier),
							new Window(threshold, threshold), Set.of(VIOLATION)),
					Arguments.of("isAtTheSameTimeAs accepts equal instants",
							(ValidationFactoryInvocation) violationSupplier -> TimeValidations.isAtTheSameTimeAs(
									Window::value, Window::threshold, violationSupplier),
							new Window(threshold, threshold), Set.of()),
					Arguments.of("isNotAfter accepts equal instants",
							(ValidationFactoryInvocation) violationSupplier -> TimeValidations.isNotAfter(
									Window::value, Window::threshold, violationSupplier),
							new Window(threshold, threshold), Set.of())
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
		void delegatesToTheRequestedComparisonType(final String as, final Window window,
		                                           final ComparisonType comparisonType,
		                                           final Set<Violation> expectedViolations)
		{
			var validation = TimeValidations.compare(Window::value, Window::threshold, comparisonType, () -> VIOLATION);

			var result = validation.validate(window);

			assertThat(result.violations())
					.as("violations for %s", as)
					.isEqualTo(expectedViolations);
		}

		private static Stream<Arguments> delegatesToTheRequestedComparisonTypeCases()
		{
			var threshold = OffsetDateTime.of(2026, 6, 17, 12, 0, 0, 0, ZoneOffset.UTC);

			return Stream.of(
					Arguments.of("greater-than accepts later instants", new Window(threshold.plusMinutes(1), threshold),
							ComparisonType.GREATER_THAN, Set.of()),
					Arguments.of("equal rejects later instants", new Window(threshold.plusMinutes(1), threshold),
							ComparisonType.EQUAL, Set.of(VIOLATION))
			);
		}
	}
}