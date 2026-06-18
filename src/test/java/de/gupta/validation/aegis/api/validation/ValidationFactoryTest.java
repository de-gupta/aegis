package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationFactory")
final class ValidationFactoryTest
{
	private static final TestViolation TRIMMED_VIOLATION = new TestViolation("must be trimmed", Severity.MEDIUM);
	private static final TestViolation COMPARISON_VIOLATION = new TestViolation("must be below threshold",
			Severity.HIGH);

	private record Sample(String name, Integer value, Integer threshold)
	{
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for from()")
	final class ForFrom
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("createsValidationFromSpecificationCases")
		@DisplayName("creates a validation that returns an empty or singleton result depending on the specification")
		void createsAValidationThatReturnsAnEmptyOrSingletonResultDependingOnTheSpecification(final String as,
		                                                                                      final FromCase tc)
		{
			Validation<Sample> validation = ValidationFactory.from(tc.extractor(), tc.specification(),
					() -> TRIMMED_VIOLATION);

			var result = validation.validate(tc.sample());

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

		private static Stream<Arguments> createsValidationFromSpecificationCases()
		{
			return Stream.of(
					FromCase.of("satisfied specification returns an empty validation result",
							Sample::name,
							value -> value != null && value.equals(value.trim()),
							new Sample("trimmed", 5, 10),
							Set.of(),
							Optional.empty()),
					FromCase.of("unsatisfied specification returns the supplied violation",
							Sample::name,
							value -> value != null && value.equals(value.trim()),
							new Sample(" not-trimmed ", 5, 10),
							Set.of(TRIMMED_VIOLATION),
							Optional.of(Severity.MEDIUM))
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record FromCase(String as, Function<Sample, String> extractor, Specification<String> specification,
		                        Sample sample, Set<Violation> expectedViolations,
		                        Optional<Severity> expectedHighestSeverity)
		{
			private static FromCase of(final String as, final Function<Sample, String> extractor,
			                           final Specification<String> specification, final Sample sample,
			                           final Set<Violation> expectedViolations,
			                           final Optional<Severity> expectedHighestSeverity)
			{
				return new FromCase(as, extractor, specification, sample, expectedViolations, expectedHighestSeverity);
			}
		}
	}

	@Nested
	@DisplayName("for fromExtractor()")
	final class ForFromExtractor
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("createsValidationFromExtractorCases")
		@DisplayName("creates a validation whose specification depends on the full object")
		void createsAValidationWhoseSpecificationDependsOnTheFullObject(final String as, final FromExtractorCase tc)
		{
			Validation<Sample> validation = ValidationFactory.fromExtractor(tc.extractor(), tc.specificationExtractor(),
					() -> COMPARISON_VIOLATION);

			ValidationResult result = validation.validate(tc.sample());

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

		private static Stream<Arguments> createsValidationFromExtractorCases()
		{
			return Stream.of(
					FromExtractorCase.of("value below the object-specific threshold produces no violation",
							Sample::value,
							sample -> candidate -> candidate < sample.threshold(),
							new Sample("trimmed", 5, 10),
							Set.of(),
							Optional.empty()),
					FromExtractorCase.of("value at the object-specific threshold produces the supplied violation",
							Sample::value,
							sample -> candidate -> candidate < sample.threshold(),
							new Sample("trimmed", 10, 10),
							Set.of(COMPARISON_VIOLATION),
							Optional.of(Severity.HIGH))
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record FromExtractorCase(String as, Function<Sample, Integer> extractor,
		                                 Function<Sample, Specification<Integer>> specificationExtractor, Sample sample,
		                                 Set<Violation> expectedViolations,
		                                 Optional<Severity> expectedHighestSeverity)
		{
			private static FromExtractorCase of(final String as, final Function<Sample, Integer> extractor,
			                                    final Function<Sample, Specification<Integer>> specificationExtractor,
			                                    final Sample sample, final Set<Violation> expectedViolations,
			                                    final Optional<Severity> expectedHighestSeverity)
			{
				return new FromExtractorCase(as, extractor, specificationExtractor, sample, expectedViolations,
						expectedHighestSeverity);
			}
		}
	}
}