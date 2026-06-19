package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.validation.aegis.api.validation.policy.ValidationPolicy;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("OutcomeOperations")
final class ValidationOutcomeOperationsTest
{
	private static final TestViolation LOW = new TestViolation("low", Severity.LOW);
	private static final TestViolation HIGH = new TestViolation("high", Severity.HIGH);

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for map()")
	final class ForMap
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("mapsSuccessfulAndValidatedOutcomesCases")
		@DisplayName("maps successful and validated outcomes to the target value while preserving metadata")
		<T, R> void mapsSuccessfulAndValidatedOutcomesToTheTargetValueWhilePreservingMetadata(final String as,
		                                                                                      final MappingCase<T, R> tc)
		{
			var result = OutcomeOperations.map(tc.validationOutcome(), tc.mapper());

			assertSoftly(softly ->
			{
				softly.assertThat(result.isSuccessful())
				      .as("success flag for %s", as)
				      .isEqualTo(true);
				softly.assertThat(result.optionalValue())
				      .as("optional value for %s", as)
				      .isEqualTo(Optional.of(tc.expectedValue()));
				softly.assertThat(((SuccessfulValidationOutcome<R>) result).value())
				      .as("mapped value for %s", as)
				      .isEqualTo(tc.expectedValue());
				softly.assertThat(result.validationResult())
				      .as("validation result for %s", as)
				      .isSameAs(tc.validationResult());
				softly.assertThat(result)
				      .as("mapped type for %s", as)
				      .isInstanceOf(tc.expectedType());
				if (tc.expectedType() == ValidatedValidationOutcome.class)
				{
					softly.assertThat(((ValidatedValidationOutcome<R>) result).policy())
					      .as("policy for %s", as)
					      .isSameAs(tc.policy());
				}
			});
		}

		@ParameterizedTest(name = "{0}")
		@MethodSource("mapsRejectedAndFailureOutcomesCases")
		@DisplayName("maps rejected and failure outcomes without invoking the mapper")
		<T, R> void mapsRejectedAndFailureOutcomesWithoutInvokingTheMapper(final String as,
		                                                                   final PreservationCase<T, R> tc)
		{
			var result = OutcomeOperations.map(tc.validationOutcome(), tc.mapper());

			assertSoftly(softly ->
			{
				softly.assertThat(result.isSuccessful())
				      .as("success flag for %s", as)
				      .isEqualTo(false);
				softly.assertThat(result.optionalValue())
				      .as("optional value for %s", as)
				      .isEmpty();
				softly.assertThat(result)
				      .as("mapped type for %s", as)
				      .isInstanceOf(tc.expectedType());
				softly.assertThat(result.validationResult())
				      .as("validation result for %s", as)
				      .isSameAs(tc.validationResult());
				if (tc.expectedType() == RejectedValidationOutcome.class)
				{
					softly.assertThat(((RejectedValidationOutcome<R>) result).policy())
					      .as("policy for %s", as)
					      .isSameAs(tc.policy());
				}
			});
		}

		@ParameterizedTest(name = "{0}")
		@MethodSource("propagatesMapperFailuresForEmptyCollectionsCases")
		@DisplayName("propagates mapper failures when finding the first element of an empty collection")
		void propagatesMapperFailuresWhenFindingTheFirstElementOfAnEmptyCollection(final String as,
		                                                                           final ExceptionalMappingCase<List<String>, String> tc)
		{
			assertThatThrownBy(() -> OutcomeOperations.map(tc.validationOutcome(), tc.mapper()))
					.as("%s", as)
					.isInstanceOf(NoSuchElementException.class);
		}

		private static Stream<Arguments> mapsSuccessfulAndValidatedOutcomesCases()
		{
			var emptyValidationResult = ValidationResultFactory.empty();
			var lowValidationResult = ValidationResultFactory.with(LOW);
			ValidationPolicy permissivePolicy = result -> result.isLessThan(Severity.HIGH);

			return Stream.of(
					MappingCase.of("successful string validationOutcome maps to integer length",
							OutcomeFactory.success("alpha", emptyValidationResult),
							String::length,
							5,
							emptyValidationResult,
							SuccessfulValidationOutcome.class,
							null),
					MappingCase.of("successful collection validationOutcome maps to one of its elements",
							OutcomeFactory.success(List.of("first", "second", "third"), lowValidationResult),
							values -> values.get(1),
							"second",
							lowValidationResult,
							SuccessfulValidationOutcome.class,
							null),
					MappingCase.of("successful empty collection validationOutcome maps to its size",
							OutcomeFactory.success(List.<String>of(), emptyValidationResult),
							List::size,
							0,
							emptyValidationResult,
							SuccessfulValidationOutcome.class,
							null),
					MappingCase.of(
							"validated collection validationOutcome maps to one of its elements and keeps the policy",
							OutcomeFactory.validated(List.of("alpha", "beta"), lowValidationResult, permissivePolicy),
							List::getFirst,
							"alpha",
							lowValidationResult,
							ValidatedValidationOutcome.class,
							permissivePolicy),
					MappingCase.of("validated empty collection validationOutcome maps to its size and keeps the policy",
							OutcomeFactory.validated(List.<String>of(), lowValidationResult, permissivePolicy),
							List::size,
							0,
							lowValidationResult,
							ValidatedValidationOutcome.class,
							permissivePolicy)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private static Stream<Arguments> mapsRejectedAndFailureOutcomesCases()
		{
			var rejectedValidationResult = ValidationResultFactory.with(Set.of(LOW, HIGH));
			var failedValidationResult = ValidationResultFactory.with(HIGH);
			ValidationPolicy strictPolicy = result -> result.isLessThan(Severity.HIGH);

			return Stream.of(
					PreservationCase.of("rejected collection validationOutcome keeps rejection and policy",
							OutcomeFactory.<List<String>>rejected(rejectedValidationResult, strictPolicy),
							failingMapper("mapper must not be invoked for rejected outcomes"),
							rejectedValidationResult,
							RejectedValidationOutcome.class,
							strictPolicy),
					PreservationCase.of("plain failure validationOutcome keeps failure without invoking the mapper",
							OutcomeFactory.<List<String>>failure(failedValidationResult),
							failingMapper("mapper must not be invoked for failure outcomes"),
							failedValidationResult,
							FailureValidationOutcome.class,
							null)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private static <T, R> Function<T, R> failingMapper(final String message)
		{
			return ignored ->
			{
				throw new AssertionError(message);
			};
		}

		private static Stream<Arguments> propagatesMapperFailuresForEmptyCollectionsCases()
		{
			var emptyValidationResult = ValidationResultFactory.empty();
			var lowValidationResult = ValidationResultFactory.with(LOW);
			ValidationPolicy permissivePolicy = result -> result.isLessThan(Severity.HIGH);

			return Stream.of(
					ExceptionalMappingCase.of(
							"successful empty collection validationOutcome throws when taking first element",
							OutcomeFactory.success(List.<String>of(), emptyValidationResult),
							List::getFirst),
					ExceptionalMappingCase.of(
							"validated empty collection validationOutcome throws when taking first element",
							OutcomeFactory.validated(List.<String>of(), lowValidationResult, permissivePolicy),
							List::getFirst)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record MappingCase<T, R>(String as, ValidationOutcome<T> validationOutcome, Function<T, R> mapper,
		                                 R expectedValue,
		                                 ValidationResult validationResult, Class<?> expectedType,
		                                 ValidationPolicy policy)
		{
			private static <T, R> MappingCase<T, R> of(final String as, final ValidationOutcome<T> validationOutcome,
			                                           final Function<T, R> mapper, final R expectedValue,
			                                           final ValidationResult validationResult,
			                                           final Class<?> expectedType, final ValidationPolicy policy)
			{
				return new MappingCase<>(as, validationOutcome, mapper, expectedValue, validationResult, expectedType,
						policy);
			}
		}

		private record PreservationCase<T, R>(String as, ValidationOutcome<T> validationOutcome, Function<T, R> mapper,
		                                      ValidationResult validationResult, Class<?> expectedType,
		                                      ValidationPolicy policy)
		{
			private static <T, R> PreservationCase<T, R> of(final String as,
			                                                final ValidationOutcome<T> validationOutcome,
			                                                final Function<T, R> mapper,
			                                                final ValidationResult validationResult,
			                                                final Class<?> expectedType, final ValidationPolicy policy)
			{
				return new PreservationCase<>(as, validationOutcome, mapper, validationResult, expectedType, policy);
			}
		}

		private record ExceptionalMappingCase<T, R>(String as, ValidationOutcome<T> validationOutcome,
		                                            Function<T, R> mapper)
		{
			private static <T, R> ExceptionalMappingCase<T, R> of(final String as,
			                                                      final ValidationOutcome<T> validationOutcome,
			                                                      final Function<T, R> mapper)
			{
				return new ExceptionalMappingCase<>(as, validationOutcome, mapper);
			}
		}
	}
}