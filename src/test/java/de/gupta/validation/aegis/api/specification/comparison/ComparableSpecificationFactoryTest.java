package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.validation.aegis.api.specification.Specification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

final class ComparableSpecificationFactoryTest
{
	private record IntegerTestCase(String description, Integer value, Integer threshold, boolean expectedResult)
	{
		static IntegerTestCase of(final String description, final Integer value, final Integer threshold,
								  final boolean expectedResult)
		{
			return new IntegerTestCase(description, value, threshold, expectedResult);
		}
	}

	private record StringTestCase(String description, String value, String threshold, boolean expectedResult)
	{
		static StringTestCase of(final String description, final String value, final String threshold,
								 final boolean expectedResult)
		{
			return new StringTestCase(description, value, threshold, expectedResult);
		}
	}

	private record BigDecimalTestCase(String description, BigDecimal value, BigDecimal threshold,
									  boolean expectedResult)
	{
		static BigDecimalTestCase of(final String description, final BigDecimal value, final BigDecimal threshold,
									 final boolean expectedResult)
		{
			return new BigDecimalTestCase(description, value, threshold, expectedResult);
		}
	}


	private record ComparisonSpecificationTestCase(String description, Integer value, Integer threshold,
												   ComparisonType comparisonType, boolean expectedResult)
	{
		static ComparisonSpecificationTestCase of(final String description, final Integer value,
												  final Integer threshold,
												  final ComparisonType comparisonType, final boolean expectedResult)
		{
			return new ComparisonSpecificationTestCase(description, value, threshold, comparisonType, expectedResult);
		}
	}

	private record FactoryMethodTestCase(String description, Specification<Integer> specification1,
										 Specification<Integer> specification2)
	{
		static FactoryMethodTestCase of(final String description, final Specification<Integer> specification1,
										final Specification<Integer> specification2)
		{
			return new FactoryMethodTestCase(description, specification1, specification2);
		}
	}

	@Nested
	@DisplayName("lessThan() specification tests")
	final class LessThanTests
	{
		@DisplayName("should validate less than comparisons correctly for integers")
		@ParameterizedTest(name = "{0}")
		@MethodSource("integerLessThanTestCases")
		void shouldValidateLessThanIntegersCorrectly(final String description, final Integer value,
													 final Integer threshold, final boolean expectedResult)
		{
			final Specification<Integer> specification = ComparableSpecificationFactory.lessThan(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("lessThan specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		@DisplayName("should validate less than comparisons correctly for strings")
		@ParameterizedTest(name = "{0}")
		@MethodSource("stringLessThanTestCases")
		void shouldValidateLessThanStringsCorrectly(final String description, final String value,
													final String threshold, final boolean expectedResult)
		{
			final Specification<String> specification = ComparableSpecificationFactory.lessThan(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("lessThan specification %s should return %s for value '%s' compared to threshold '%s'",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		@DisplayName("should validate less than comparisons correctly for BigDecimal")
		@ParameterizedTest(name = "{0}")
		@MethodSource("bigDecimalLessThanTestCases")
		void shouldValidateLessThanBigDecimalCorrectly(final String description, final BigDecimal value,
													   final BigDecimal threshold, final boolean expectedResult)
		{
			final Specification<BigDecimal> specification = ComparableSpecificationFactory.lessThan(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("lessThan specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> integerLessThanTestCases()
		{
			return Stream.of(
					IntegerTestCase.of("with value less than threshold", 5, 10, true),
					IntegerTestCase.of("with value equal to threshold", 10, 10, false),
					IntegerTestCase.of("with value greater than threshold", 15, 10, false),
					IntegerTestCase.of("with negative value less than positive threshold", -5, 10, true),
					IntegerTestCase.of("with negative value less than negative threshold", -15, -10, true),
					IntegerTestCase.of("with negative value equal to negative threshold", -10, -10, false),
					IntegerTestCase.of("with negative value greater than negative threshold", -5, -10, false),
					IntegerTestCase.of("with positive value less than negative threshold", 5, -10, false),
					IntegerTestCase.of("with zero less than positive threshold", 0, 10, true),
					IntegerTestCase.of("with zero less than negative threshold", 0, -10, false),
					IntegerTestCase.of("with zero equal to zero threshold", 0, 0, false),
					IntegerTestCase.of("with minimum integer value", Integer.MIN_VALUE, 0, true),
					IntegerTestCase.of("with maximum integer value", Integer.MAX_VALUE, 0, false),
					IntegerTestCase.of("with null value", null, 10, false),
					IntegerTestCase.of("with null threshold", 10, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}

		private static Stream<Arguments> stringLessThanTestCases()
		{
			return Stream.of(
					StringTestCase.of("with value lexicographically less than threshold", "apple", "banana", true),
					StringTestCase.of("with value lexicographically equal to threshold", "apple", "apple", false),
					StringTestCase.of("with value lexicographically greater than threshold", "banana", "apple", false),
					StringTestCase.of("with empty string less than non-empty", "", "apple", true),
					StringTestCase.of("with non-empty string greater than empty", "apple", "", false),
					StringTestCase.of("with both empty strings", "", "", false),
					StringTestCase.of("with case sensitivity - lowercase less than uppercase", "apple", "BANANA",
							false),
					StringTestCase.of("with case sensitivity - uppercase less than lowercase", "APPLE", "banana", true),
					StringTestCase.of("with numeric strings", "10", "2", true),
					StringTestCase.of("with special characters", "!", "@", true),
					StringTestCase.of("with null value", null, "apple", false),
					StringTestCase.of("with null threshold", "apple", null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}

		private static Stream<Arguments> bigDecimalLessThanTestCases()
		{
			return Stream.of(
					BigDecimalTestCase.of("with value less than threshold", new BigDecimal("5.5"),
							new BigDecimal("10.0"), true),
					BigDecimalTestCase.of("with value equal to threshold", new BigDecimal("10.0"),
							new BigDecimal("10.0"), false),
					BigDecimalTestCase.of("with value greater than threshold", new BigDecimal("15.5"),
							new BigDecimal("10.0"), false),
					BigDecimalTestCase.of("with different scale but equal values", new BigDecimal("10.00"),
							new BigDecimal("10.0"), false),
					BigDecimalTestCase.of("with very small difference", new BigDecimal("9.999999"),
							new BigDecimal("10.000000"), true),
					BigDecimalTestCase.of("with zero values", BigDecimal.ZERO, BigDecimal.ZERO, false),
					BigDecimalTestCase.of("with negative values", new BigDecimal("-5.5"), new BigDecimal("-2.2"), true),
					BigDecimalTestCase.of("with null value", null, new BigDecimal("10.0"), false),
					BigDecimalTestCase.of("with null threshold", new BigDecimal("10.0"), null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("lessThanOrEqualTo() specification tests")
	final class LessThanOrEqualToTests
	{
		@DisplayName("should validate less than or equal to comparisons correctly for integers")
		@ParameterizedTest(name = "{0}")
		@MethodSource("integerLessThanOrEqualToTestCases")
		void shouldValidateLessThanOrEqualToIntegersCorrectly(final String description, final Integer value,
															  final Integer threshold, final boolean expectedResult)
		{
			final Specification<Integer> specification = ComparableSpecificationFactory.lessThanOrEqualTo(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("lessThanOrEqualTo specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}


		private static Stream<Arguments> integerLessThanOrEqualToTestCases()
		{
			return Stream.of(
					IntegerTestCase.of("with value less than threshold", 5, 10, true),
					IntegerTestCase.of("with value equal to threshold", 10, 10, true),
					IntegerTestCase.of("with value greater than threshold", 15, 10, false),
					IntegerTestCase.of("with negative value less than negative threshold", -15, -10, true),
					IntegerTestCase.of("with negative value equal to negative threshold", -10, -10, true),
					IntegerTestCase.of("with negative value greater than negative threshold", -5, -10, false),
					IntegerTestCase.of("with zero equal to zero threshold", 0, 0, true),
					IntegerTestCase.of("with minimum integer value", Integer.MIN_VALUE, Integer.MIN_VALUE, true),
					IntegerTestCase.of("with maximum integer value", Integer.MAX_VALUE, Integer.MAX_VALUE, true),
					IntegerTestCase.of("with null value", null, 10, false),
					IntegerTestCase.of("with null threshold", 10, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}

	}

	@Nested
	@DisplayName("greaterThan() specification tests")
	final class GreaterThanTests
	{
		@DisplayName("should validate greater than comparisons correctly for integers")
		@ParameterizedTest(name = "{0}")
		@MethodSource("integerGreaterThanTestCases")
		void shouldValidateGreaterThanIntegersCorrectly(final String description, final Integer value,
														final Integer threshold, final boolean expectedResult)
		{
			final Specification<Integer> specification = ComparableSpecificationFactory.greaterThan(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("greaterThan specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> integerGreaterThanTestCases()
		{
			return Stream.of(
					IntegerTestCase.of("with value greater than threshold", 15, 10, true),
					IntegerTestCase.of("with value equal to threshold", 10, 10, false),
					IntegerTestCase.of("with value less than threshold", 5, 10, false),
					IntegerTestCase.of("with negative value greater than negative threshold", -5, -10, true),
					IntegerTestCase.of("with negative value equal to negative threshold", -10, -10, false),
					IntegerTestCase.of("with negative value less than negative threshold", -15, -10, false),
					IntegerTestCase.of("with positive value greater than negative threshold", 5, -10, true),
					IntegerTestCase.of("with zero greater than negative threshold", 0, -10, true),
					IntegerTestCase.of("with zero greater than positive threshold", 0, 10, false),
					IntegerTestCase.of("with zero equal to zero threshold", 0, 0, false),
					IntegerTestCase.of("with maximum integer value", Integer.MAX_VALUE, 0, true),
					IntegerTestCase.of("with minimum integer value", Integer.MIN_VALUE, 0, false),
					IntegerTestCase.of("with null value", null, 10, false),
					IntegerTestCase.of("with null threshold", 10, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("greaterThanOrEqualTo() specification tests")
	final class GreaterThanOrEqualToTests
	{
		@DisplayName("should validate greater than or equal to comparisons correctly for integers")
		@ParameterizedTest(name = "{0}")
		@MethodSource("integerGreaterThanOrEqualToTestCases")
		void shouldValidateGreaterThanOrEqualToIntegersCorrectly(final String description, final Integer value,
																 final Integer threshold, final boolean expectedResult)
		{
			final Specification<Integer> specification = ComparableSpecificationFactory.greaterThanOrEqualTo(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("greaterThanOrEqualTo specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> integerGreaterThanOrEqualToTestCases()
		{
			return Stream.of(
					IntegerTestCase.of("with value greater than threshold", 15, 10, true),
					IntegerTestCase.of("with value equal to threshold", 10, 10, true),
					IntegerTestCase.of("with value less than threshold", 5, 10, false),
					IntegerTestCase.of("with negative value greater than negative threshold", -5, -10, true),
					IntegerTestCase.of("with negative value equal to negative threshold", -10, -10, true),
					IntegerTestCase.of("with negative value less than negative threshold", -15, -10, false),
					IntegerTestCase.of("with zero equal to zero threshold", 0, 0, true),
					IntegerTestCase.of("with minimum integer value", Integer.MIN_VALUE, Integer.MIN_VALUE, true),
					IntegerTestCase.of("with maximum integer value", Integer.MAX_VALUE, Integer.MAX_VALUE, true),
					IntegerTestCase.of("with null value", null, 10, false),
					IntegerTestCase.of("with null threshold", 10, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("equal() specification tests")
	final class EqualTests
	{
		@DisplayName("should validate equality comparisons correctly for integers")
		@ParameterizedTest(name = "{0}")
		@MethodSource("integerEqualTestCases")
		void shouldValidateEqualIntegersCorrectly(final String description, final Integer value,
												  final Integer threshold, final boolean expectedResult)
		{
			final Specification<Integer> specification = ComparableSpecificationFactory.equal(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("equal specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		@DisplayName("should validate equality comparisons correctly for strings")
		@ParameterizedTest(name = "{0}")
		@MethodSource("stringEqualTestCases")
		void shouldValidateEqualStringsCorrectly(final String description, final String value,
												 final String threshold, final boolean expectedResult)
		{
			final Specification<String> specification = ComparableSpecificationFactory.equal(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("equal specification %s should return %s for value '%s' compared to threshold '%s'",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> integerEqualTestCases()
		{
			return Stream.of(
					IntegerTestCase.of("with equal positive values", 10, 10, true),
					IntegerTestCase.of("with unequal positive values", 10, 15, false),
					IntegerTestCase.of("with equal negative values", -10, -10, true),
					IntegerTestCase.of("with unequal negative values", -10, -15, false),
					IntegerTestCase.of("with equal zero values", 0, 0, true),
					IntegerTestCase.of("with positive and negative values", 10, -10, false),
					IntegerTestCase.of("with zero and positive value", 0, 10, false),
					IntegerTestCase.of("with zero and negative value", 0, -10, false),
					IntegerTestCase.of("with maximum integer values", Integer.MAX_VALUE, Integer.MAX_VALUE, true),
					IntegerTestCase.of("with minimum integer values", Integer.MIN_VALUE, Integer.MIN_VALUE, true),
					IntegerTestCase.of("with null value", null, 10, false),
					IntegerTestCase.of("with null threshold", 10, null, false),
					IntegerTestCase.of("with both null values", null, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}

		private static Stream<Arguments> stringEqualTestCases()
		{
			return Stream.of(
					StringTestCase.of("with equal strings", "apple", "apple", true),
					StringTestCase.of("with unequal strings", "apple", "banana", false),
					StringTestCase.of("with equal empty strings", "", "", true),
					StringTestCase.of("with empty and non-empty strings", "", "apple", false),
					StringTestCase.of("with case sensitive comparison", "Apple", "apple", false),
					StringTestCase.of("with whitespace strings", " ", " ", true),
					StringTestCase.of("with different whitespace", " ", "  ", false),
					StringTestCase.of("with special characters", "@#$", "@#$", true),
					StringTestCase.of("with numeric strings", "123", "123", true),
					StringTestCase.of("with null value", null, "apple", false),
					StringTestCase.of("with null threshold", "apple", null, false),
					StringTestCase.of("with both null values", null, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("notEqual() specification tests")
	final class NotEqualTests
	{
		@DisplayName("should validate not equal comparisons correctly for integers")
		@ParameterizedTest(name = "{0}")
		@MethodSource("integerNotEqualTestCases")
		void shouldValidateNotEqualIntegersCorrectly(final String description, final Integer value,
													 final Integer threshold, final boolean expectedResult)
		{
			final Specification<Integer> specification = ComparableSpecificationFactory.notEqual(threshold);

			assertThat(specification.isSatisfiedBy(value))
					.as("notEqual specification %s should return %s for value %s compared to threshold %s",
							description, expectedResult, value, threshold)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> integerNotEqualTestCases()
		{
			return Stream.of(
					IntegerTestCase.of("with equal positive values", 10, 10, false),
					IntegerTestCase.of("with unequal positive values", 10, 15, true),
					IntegerTestCase.of("with equal negative values", -10, -10, false),
					IntegerTestCase.of("with unequal negative values", -10, -15, true),
					IntegerTestCase.of("with equal zero values", 0, 0, false),
					IntegerTestCase.of("with positive and negative values", 10, -10, true),
					IntegerTestCase.of("with zero and positive value", 0, 10, true),
					IntegerTestCase.of("with zero and negative value", 0, -10, true),
					IntegerTestCase.of("with maximum integer values", Integer.MAX_VALUE, Integer.MAX_VALUE, false),
					IntegerTestCase.of("with minimum integer values", Integer.MIN_VALUE, Integer.MIN_VALUE, false),
					IntegerTestCase.of("with null value", null, 10, false),
					IntegerTestCase.of("with null threshold", 10, null, false),
					IntegerTestCase.of("with both null values", null, null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("comparisonSpecification() specification tests")
	final class ComparisonSpecificationTests
	{
		@DisplayName("should validate comparison specifications correctly for all comparison types")
		@ParameterizedTest(name = "{0}")
		@MethodSource("comparisonSpecificationTestCases")
		void shouldValidateComparisonSpecificationsCorrectly(final String description, final Integer value,
															 final Integer threshold,
															 final ComparisonType comparisonType,
															 final boolean expectedResult)
		{
			final Specification<Integer> specification =
					ComparableSpecificationFactory.comparisonSpecification(threshold, comparisonType);

			assertThat(specification.isSatisfiedBy(value))
					.as("comparisonSpecification %s should return %s for value %s compared to threshold %s using %s",
							description, expectedResult, value, threshold, comparisonType)
					.isEqualTo(expectedResult);
		}


		private static Stream<Arguments> comparisonSpecificationTestCases()
		{
			return Stream.of(
					ComparisonSpecificationTestCase.of("LESS_THAN with value less than threshold", 5, 10,
							ComparisonType.LESS_THAN, true),
					ComparisonSpecificationTestCase.of("LESS_THAN with value equal to threshold", 10, 10,
							ComparisonType.LESS_THAN, false),
					ComparisonSpecificationTestCase.of("LESS_THAN with value greater than threshold", 15, 10,
							ComparisonType.LESS_THAN, false),
					ComparisonSpecificationTestCase.of("LESS_THAN_OR_EQUAL with value less than threshold", 5, 10,
							ComparisonType.LESS_THAN_OR_EQUAL, true),
					ComparisonSpecificationTestCase.of("LESS_THAN_OR_EQUAL with value equal to threshold", 10, 10,
							ComparisonType.LESS_THAN_OR_EQUAL, true),
					ComparisonSpecificationTestCase.of("LESS_THAN_OR_EQUAL with value greater than threshold", 15, 10,
							ComparisonType.LESS_THAN_OR_EQUAL, false),
					ComparisonSpecificationTestCase.of("EQUAL with value less than threshold", 5, 10,
							ComparisonType.EQUAL, false),
					ComparisonSpecificationTestCase.of("EQUAL with value equal to threshold", 10, 10,
							ComparisonType.EQUAL, true),
					ComparisonSpecificationTestCase.of("EQUAL with value greater than threshold", 15, 10,
							ComparisonType.EQUAL, false),
					ComparisonSpecificationTestCase.of("GREATER_THAN_OR_EQUAL with value less than threshold", 5, 10,
							ComparisonType.GREATER_THAN_OR_EQUAL, false),
					ComparisonSpecificationTestCase.of("GREATER_THAN_OR_EQUAL with value equal to threshold", 10, 10,
							ComparisonType.GREATER_THAN_OR_EQUAL, true),
					ComparisonSpecificationTestCase.of("GREATER_THAN_OR_EQUAL with value greater than threshold", 15,
							10, ComparisonType.GREATER_THAN_OR_EQUAL, true),
					ComparisonSpecificationTestCase.of("GREATER_THAN with value less than threshold", 5, 10,
							ComparisonType.GREATER_THAN, false),
					ComparisonSpecificationTestCase.of("GREATER_THAN with value equal to threshold", 10, 10,
							ComparisonType.GREATER_THAN, false),
					ComparisonSpecificationTestCase.of("GREATER_THAN with value greater than threshold", 15, 10,
							ComparisonType.GREATER_THAN, true),
					ComparisonSpecificationTestCase.of("with negative values and LESS_THAN", -15, -10,
							ComparisonType.LESS_THAN, true),
					ComparisonSpecificationTestCase.of("with zero values and EQUAL", 0, 0, ComparisonType.EQUAL, true),
					ComparisonSpecificationTestCase.of("with null value", null, 10, ComparisonType.EQUAL, false),
					ComparisonSpecificationTestCase.of("with null threshold", 10, null, ComparisonType.EQUAL, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.threshold, tc.comparisonType, tc.expectedResult));
		}

	}

	@Nested
	@DisplayName("Factory method tests")
	final class FactoryMethodTests
	{
		@DisplayName("should create different specification instances")
		@ParameterizedTest(name = "{0}")
		@MethodSource("factoryMethodTestCases")
		void shouldCreateDifferentSpecificationInstances(final String description,
														 final Specification<Integer> specification1,
														 final Specification<Integer> specification2)
		{
			assertThat(specification1)
					.as("First specification instance %s should not be null", description)
					.isNotNull();

			assertThat(specification2)
					.as("Second specification instance %s should not be null", description)
					.isNotNull();

			assertThat(specification1)
					.as("Different calls to factory methods %s should create different instances", description)
					.isNotSameAs(specification2);
		}

		@DisplayName("should create non-null specifications for all methods")
		@ParameterizedTest(name = "{0}")
		@MethodSource("allFactoryMethodTestCases")
		void shouldCreateNonNullSpecificationsForAllMethods(final String description,
															final Specification<Integer> specification)
		{
			assertThat(specification)
					.as("Specification created by %s should not be null", description)
					.isNotNull();
		}

		private static Stream<Arguments> factoryMethodTestCases()
		{
			final Integer threshold = 10;
			return Stream.of(
					FactoryMethodTestCase.of("for lessThan()",
							ComparableSpecificationFactory.lessThan(threshold),
							ComparableSpecificationFactory.lessThan(threshold)),
					FactoryMethodTestCase.of("for lessThanOrEqualTo()",
							ComparableSpecificationFactory.lessThanOrEqualTo(threshold),
							ComparableSpecificationFactory.lessThanOrEqualTo(threshold)),
					FactoryMethodTestCase.of("for greaterThan()",
							ComparableSpecificationFactory.greaterThan(threshold),
							ComparableSpecificationFactory.greaterThan(threshold)),
					FactoryMethodTestCase.of("for greaterThanOrEqualTo()",
							ComparableSpecificationFactory.greaterThanOrEqualTo(threshold),
							ComparableSpecificationFactory.greaterThanOrEqualTo(threshold)),
					FactoryMethodTestCase.of("for equal()",
							ComparableSpecificationFactory.equal(threshold),
							ComparableSpecificationFactory.equal(threshold)),
					FactoryMethodTestCase.of("for notEqual()",
							ComparableSpecificationFactory.notEqual(threshold),
							ComparableSpecificationFactory.notEqual(threshold)),
					FactoryMethodTestCase.of("between different methods lessThan() and greaterThan()",
							ComparableSpecificationFactory.lessThan(threshold),
							ComparableSpecificationFactory.greaterThan(threshold))
			).map(tc -> Arguments.of(tc.description, tc.specification1, tc.specification2));
		}

		private static Stream<Arguments> allFactoryMethodTestCases()
		{
			final Integer threshold = 10;
			return Stream.of(
					Arguments.of("lessThan()", ComparableSpecificationFactory.lessThan(threshold)),
					Arguments.of("lessThanOrEqualTo()", ComparableSpecificationFactory.lessThanOrEqualTo(threshold)),
					Arguments.of("greaterThan()", ComparableSpecificationFactory.greaterThan(threshold)),
					Arguments.of("greaterThanOrEqualTo()",
							ComparableSpecificationFactory.greaterThanOrEqualTo(threshold)),
					Arguments.of("equal()", ComparableSpecificationFactory.equal(threshold)),
					Arguments.of("notEqual()", ComparableSpecificationFactory.notEqual(threshold)),
					Arguments.of("comparisonSpecification() with LESS_THAN",
							ComparableSpecificationFactory.comparisonSpecification(threshold,
									ComparisonType.LESS_THAN)),
					Arguments.of("comparisonSpecification() with EQUAL",
							ComparableSpecificationFactory.comparisonSpecification(threshold, ComparisonType.EQUAL)),
					Arguments.of("comparisonSpecification() with GREATER_THAN",
							ComparableSpecificationFactory.comparisonSpecification(threshold,
									ComparisonType.GREATER_THAN))
			);
		}
	}
}
