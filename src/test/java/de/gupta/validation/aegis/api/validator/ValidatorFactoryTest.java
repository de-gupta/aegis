package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.validation.Validation;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.validation.validator.Validator;
import de.gupta.validation.aegis.api.validation.validator.ValidatorFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidatorFactory")
final class ValidatorFactoryTest
{
	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for with(Collection)")
	final class ForWithCollection
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("returnsAValidatorThatCombinesTheValidationResultsOfTheSuppliedValidationsCases")
		@DisplayName("returns a validator that combines the validation results of the supplied validations")
		void returnsAValidatorThatCombinesTheValidationResultsOfTheSuppliedValidations(final String as,
		                                                                               final WithCollectionCase tc)
		{
			var validator = validator();
			var result = validator.validate(tc.value());

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

		private static Validator<String> validator()
		{
			Validation<String> blankValidation = value -> value.isBlank()
					? ValidationResultFactory.with(new TestViolation("must not be blank", Severity.LOW))
					: ValidationResultFactory.empty();
			Validation<String> shortValidation = value -> value.length() < 3
					?
					ValidationResultFactory.with(new TestViolation("must be at least three characters", Severity.HIGH))
					: ValidationResultFactory.empty();

			return ValidatorFactory.with(List.of(blankValidation, shortValidation));
		}

		private static Stream<Arguments> returnsAValidatorThatCombinesTheValidationResultsOfTheSuppliedValidationsCases()
		{
			return Stream.of(
					WithCollectionCase.of("blank value triggers both validations",
							" ",
							Set.of(
									new TestViolation("must not be blank", Severity.LOW),
									new TestViolation("must be at least three characters", Severity.HIGH)),
							Optional.of(Severity.HIGH)),
					WithCollectionCase.of("short non-blank value triggers only the length validation",
							"ab",
							Set.of(new TestViolation("must be at least three characters", Severity.HIGH)),
							Optional.of(Severity.HIGH)),
					WithCollectionCase.of("valid value yields an empty validation result",
							"abcd",
							Set.of(),
							Optional.empty())
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record WithCollectionCase(String as, String value, Set<Violation> expectedViolations,
		                                  Optional<Severity> expectedHighestSeverity)
		{
			private static WithCollectionCase of(final String as, final String value,
			                                     final Set<Violation> expectedViolations,
			                                     final Optional<Severity> expectedHighestSeverity)
			{
				return new WithCollectionCase(as, value, expectedViolations, expectedHighestSeverity);
			}
		}
	}
}