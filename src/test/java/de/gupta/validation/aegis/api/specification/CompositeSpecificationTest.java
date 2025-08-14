package de.gupta.validation.aegis.api.specification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

final class CompositeSpecificationTest
{
	@Nested
	@DisplayName("Factory Method Tests")
	final class FactoryMethodTests
	{
		@ParameterizedTest(name = "{3}")
		@MethodSource("factoryTestCases")
		@DisplayName("should create composite specification with correct properties")
		void shouldCreateCompositeSpecificationWithCorrectProperties(Specification<String> left,
																	 Specification<String> right,
																	 CompositeSpecification.CompositionType compositionType,
																	 String description)
		{
			var result = CompositeSpecification.of(left, right, compositionType);

			assertThat(result.left()).as("Left specification should match for %s", description).isSameAs(left);

			assertThat(result.right()).as("Right specification should match for %s", description).isSameAs(right);

			assertThat(result.compositionType()).as("Composition type should match for %s", description)
												.isEqualTo(compositionType);
		}

		private static Stream<Arguments> factoryTestCases()
		{
			var alwaysTrue = (Specification<String>) _ -> true;
			var alwaysFalse = (Specification<String>) _ -> false;
			var isNull = (Specification<String>) Objects::isNull;

			return Stream.of(new FactoryTestCase(alwaysTrue, alwaysFalse, CompositeSpecification.CompositionType.AND,
										 "AND composition"),
								 new FactoryTestCase(alwaysTrue, alwaysFalse, CompositeSpecification.CompositionType.OR,
										 "OR composition"),
								 new FactoryTestCase(alwaysTrue, alwaysFalse, CompositeSpecification.CompositionType.XOR,
										 "XOR composition"),
								 new FactoryTestCase(isNull, alwaysTrue, CompositeSpecification.CompositionType.AND,
										 "null check with always true"),
								 new FactoryTestCase(isNull, alwaysFalse, CompositeSpecification.CompositionType.OR,
										 "null check with always false"))
						 .map(tc -> Arguments.of(tc.left, tc.right, tc.compositionType, tc.description));
		}

		private record FactoryTestCase(Specification<String> left, Specification<String> right,
									   CompositeSpecification.CompositionType compositionType, String description)
		{
		}
	}

	@Nested
	@DisplayName("AND Composition Tests")
	final class AndCompositionTests
	{
		@ParameterizedTest(name = "{4}")
		@MethodSource("andCompositionTestCases")
		@DisplayName("should return correct result for AND composition")
		void shouldReturnCorrectResultForAndComposition(boolean leftResult, boolean rightResult, boolean expectedResult,
														final String testValue, final String description)
		{
			var leftSpec = (Specification<String>) _ -> leftResult;
			var rightSpec = (Specification<String>) _ -> rightResult;
			var composite = CompositeSpecification.of(leftSpec, rightSpec, CompositeSpecification.CompositionType.AND);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("AND composition should return %s when %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> andCompositionTestCases()
		{
			return Stream.of(new AndTestCase(true, true, true, "both-true", "both true should produce true result"),
								 new AndTestCase(true, false, false, "left-true", "true and false should produce false result"),
								 new AndTestCase(false, true, false, "right-true", "false and true should produce false result"),
								 new AndTestCase(false, false, false, "both-false", "both false should produce false result"))
						 .map(tc -> Arguments.of(tc.leftResult, tc.rightResult, tc.expectedResult, tc.testValue,
								 tc.description));
		}

		private record AndTestCase(boolean leftResult, boolean rightResult, boolean expectedResult, String testValue,
								   String description)
		{
		}
	}

	@Nested
	@DisplayName("OR Composition Tests")
	final class OrCompositionTests
	{
		@ParameterizedTest(name = "{4}")
		@MethodSource("orCompositionTestCases")
		@DisplayName("should return correct result for OR composition")
		void shouldReturnCorrectResultForOrComposition(boolean leftResult, boolean rightResult, boolean expectedResult,
													   String testValue, String description)
		{
			var leftSpec = (Specification<String>) _ -> leftResult;
			var rightSpec = (Specification<String>) _ -> rightResult;
			var composite = CompositeSpecification.of(leftSpec, rightSpec, CompositeSpecification.CompositionType.OR);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("OR composition should return %s when %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> orCompositionTestCases()
		{
			return Stream.of(new OrTestCase(true, true, true, "both-true", "both true should produce true result"),
								 new OrTestCase(true, false, true, "left-true", "true or false should produce true result"),
								 new OrTestCase(false, true, true, "right-true", "false or true should produce true result"),
								 new OrTestCase(false, false, false, "both-false", "both false should produce false result"))
						 .map(tc -> Arguments.of(tc.leftResult, tc.rightResult, tc.expectedResult, tc.testValue,
								 tc.description));
		}

		private record OrTestCase(boolean leftResult, boolean rightResult, boolean expectedResult, String testValue,
								  String description)
		{
		}
	}

	@Nested
	@DisplayName("XOR Composition Tests")
	final class XorCompositionTests
	{
		@ParameterizedTest(name = "{4}")
		@MethodSource("xorCompositionTestCases")
		@DisplayName("should return correct result for XOR composition")
		void shouldReturnCorrectResultForXorComposition(boolean leftResult, boolean rightResult, boolean expectedResult,
														String testValue, String description)
		{
			var leftSpec = (Specification<String>) _ -> leftResult;
			var rightSpec = (Specification<String>) _ -> rightResult;
			var composite = CompositeSpecification.of(leftSpec, rightSpec, CompositeSpecification.CompositionType.XOR);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("XOR composition should return %s when %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> xorCompositionTestCases()
		{
			return Stream.of(new XorTestCase(true, true, false, "both-true", "both true should produce false result"),
								 new XorTestCase(true, false, true, "left-true", "true xor false should produce true result"),
								 new XorTestCase(false, true, true, "right-true", "false xor true should produce true result"),
								 new XorTestCase(false, false, false, "both-false", "both false should produce false result"))
						 .map(tc -> Arguments.of(tc.leftResult, tc.rightResult, tc.expectedResult, tc.testValue,
								 tc.description));
		}

		private record XorTestCase(boolean leftResult, boolean rightResult, boolean expectedResult, String testValue,
								   String description)
		{
		}
	}

	@Nested
	@DisplayName("Real World Scenario Tests")
	final class RealWorldScenarioTests
	{
		@ParameterizedTest(name = "{5}")
		@MethodSource("realWorldScenarioTestCases")
		@DisplayName("should handle real world scenarios correctly")
		void shouldHandleRealWorldScenariosCorrectly(final String testValue,
													 Specification<String> leftSpec,
													 Specification<String> rightSpec,
													 CompositeSpecification.CompositionType compositionType,
													 boolean expectedResult, String description)
		{
			var composite = CompositeSpecification.of(leftSpec, rightSpec, compositionType);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("Real world scenario should return %s for %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> realWorldScenarioTestCases()
		{
			var isNotNull = (Specification<String>) Objects::nonNull;
			var hasLengthGreaterThanThree = (Specification<String>) s -> s != null && s.length() > 3;

			return Stream.of(new ScenarioTestCase("hello", isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.AND, true,
										 "non-null string with length > 3 AND composition"),
								 new ScenarioTestCase("hi", isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.AND, false,
										 "non-null string with length <= 3 AND composition"),
								 new ScenarioTestCase(null, isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.AND, false,
										 "null string AND composition"),
								 new ScenarioTestCase("hello", isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.OR, true,
										 "non-null string with length > 3 OR composition"),
								 new ScenarioTestCase("hi", isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.OR, true,
										 "non-null string with length <= 3 OR composition"),
								 new ScenarioTestCase(null, isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.OR, false,
										 "null string OR composition"),
								 new ScenarioTestCase("hello", isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.XOR, false,
										 "non-null string with length > 3 XOR composition"),
								 new ScenarioTestCase("hi", isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.XOR, true,
										 "non-null string with length <= 3 XOR composition"),
								 new ScenarioTestCase(null, isNotNull, hasLengthGreaterThanThree,
										 CompositeSpecification.CompositionType.XOR, false,
										 "null string XOR composition"))
						 .map(tc -> Arguments.of(tc.testValue, tc.leftSpec, tc.rightSpec, tc.compositionType,
								 tc.expectedResult, tc.description));
		}

		private record ScenarioTestCase(String testValue, Specification<String> leftSpec,
										Specification<String> rightSpec,
										CompositeSpecification.CompositionType compositionType, boolean expectedResult,
										String description)
		{
		}
	}

	@Nested
	@DisplayName("LocalDate Composition Tests")
	final class LocalDateCompositionTests
	{
		@ParameterizedTest(name = "{5}")
		@MethodSource("localDateCompositionTestCases")
		@DisplayName("should handle LocalDate compositions correctly")
		void shouldHandleLocalDateCompositionsCorrectly(final LocalDate testValue,
														Specification<LocalDate> leftSpec,
														Specification<LocalDate> rightSpec,
														final CompositeSpecification.CompositionType compositionType,
														boolean expectedResult, final String description)
		{
			var composite = CompositeSpecification.of(leftSpec, rightSpec, compositionType);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("LocalDate composition should return %s for %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> localDateCompositionTestCases()
		{
			Specification<LocalDate> isAfter2023 = date -> date != null && date.isAfter(LocalDate.of(2023, 12, 31));
			Specification<LocalDate> isWeekend = date -> date != null &&
					(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);

			return Stream.of(
								 new LocalDateTestCase(LocalDate.of(2024, 1, 6), isAfter2023, isWeekend,
										 CompositeSpecification.CompositionType.AND, true,
										 "date after 2023 and weekend should produce true result"),
								 new LocalDateTestCase(LocalDate.of(2024, 1, 8), isAfter2023, isWeekend,
										 CompositeSpecification.CompositionType.AND, false,
										 "date after 2023 and weekday should produce false result"),
								 new LocalDateTestCase(LocalDate.of(2023, 1, 7), isAfter2023, isWeekend,
										 CompositeSpecification.CompositionType.AND, false,
										 "date before 2024 and weekend should produce false result"),
								 new LocalDateTestCase(LocalDate.of(2024, 1, 6), isAfter2023, isWeekend,
										 CompositeSpecification.CompositionType.OR, true,
										 "date after 2023 or weekend should produce true result"),
								 new LocalDateTestCase(LocalDate.of(2023, 1, 6), isAfter2023, isWeekend,
										 CompositeSpecification.CompositionType.OR, false,
										 "date before 2024 or weekday should produce false result"),
								 new LocalDateTestCase(null, isAfter2023, isWeekend, CompositeSpecification.CompositionType.AND,
										 false,
										 "null date with AND should produce false result"),
								 new LocalDateTestCase(null, isAfter2023, isWeekend, CompositeSpecification.CompositionType.OR,
										 false,
										 "null date with OR should produce false result"))
						 .map(tc -> Arguments.of(tc.testValue, tc.leftSpec, tc.rightSpec, tc.compositionType,
								 tc.expectedResult, tc.description));
		}

		private record LocalDateTestCase(LocalDate testValue, Specification<LocalDate> leftSpec,
										 Specification<LocalDate> rightSpec,
										 CompositeSpecification.CompositionType compositionType, boolean expectedResult,
										 String description)
		{
		}
	}

	@Nested
	@DisplayName("DayOfWeek Composition Tests")
	final class DayOfWeekCompositionTests
	{
		@ParameterizedTest(name = "{5}")
		@MethodSource("dayOfWeekCompositionTestCases")
		@DisplayName("should handle DayOfWeek compositions correctly")
		void shouldHandleDayOfWeekCompositionsCorrectly(DayOfWeek testValue,
														Specification<DayOfWeek> leftSpec,
														Specification<DayOfWeek> rightSpec,
														CompositeSpecification.CompositionType compositionType,
														boolean expectedResult, String description)
		{
			var composite = CompositeSpecification.of(leftSpec, rightSpec, compositionType);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("DayOfWeek composition should return %s for %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> dayOfWeekCompositionTestCases()
		{
			var isWeekend = (Specification<DayOfWeek>) day -> (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
			var isMonday = (Specification<DayOfWeek>) day -> day == DayOfWeek.MONDAY;

			return Stream.of(
								 new DayOfWeekTestCase(DayOfWeek.SATURDAY, isWeekend, isMonday,
										 CompositeSpecification.CompositionType.AND, false,
										 "weekend and monday should produce false result"),
								 new DayOfWeekTestCase(DayOfWeek.MONDAY, isWeekend, isMonday,
										 CompositeSpecification.CompositionType.AND, false,
										 "weekday and monday should produce false result"),
								 new DayOfWeekTestCase(DayOfWeek.SATURDAY, isWeekend, isMonday,
										 CompositeSpecification.CompositionType.OR, true,
										 "weekend or monday should produce true result"),
								 new DayOfWeekTestCase(DayOfWeek.MONDAY, isWeekend, isMonday,
										 CompositeSpecification.CompositionType.OR, true,
										 "weekday or monday should produce true result"),
								 new DayOfWeekTestCase(DayOfWeek.TUESDAY, isWeekend, isMonday,
										 CompositeSpecification.CompositionType.OR, false,
										 "tuesday or monday should produce false result"),
								 new DayOfWeekTestCase(DayOfWeek.SATURDAY, isWeekend, isMonday,
										 CompositeSpecification.CompositionType.XOR, true,
										 "weekend xor monday should produce true result"),
								 new DayOfWeekTestCase(null, isWeekend, isMonday, CompositeSpecification.CompositionType.AND, false,
										 "null day with AND should produce false result"))
						 .map(tc -> Arguments.of(tc.testValue, tc.leftSpec, tc.rightSpec, tc.compositionType,
								 tc.expectedResult, tc.description));
		}

		private record DayOfWeekTestCase(DayOfWeek testValue, Specification<DayOfWeek> leftSpec,
										 Specification<DayOfWeek> rightSpec,
										 CompositeSpecification.CompositionType compositionType, boolean expectedResult,
										 String description)
		{
		}
	}

	@Nested
	@DisplayName("BigDecimal Composition Tests")
	final class BigDecimalCompositionTests
	{
		@ParameterizedTest(name = "{5}")
		@MethodSource("bigDecimalCompositionTestCases")
		@DisplayName("should handle BigDecimal compositions correctly")
		void shouldHandleBigDecimalCompositionsCorrectly(BigDecimal testValue,
														 Specification<BigDecimal> leftSpec,
														 Specification<BigDecimal> rightSpec,
														 CompositeSpecification.CompositionType compositionType,
														 boolean expectedResult, String description)
		{
			var composite = CompositeSpecification.of(leftSpec, rightSpec, compositionType);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("BigDecimal composition should return %s for %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> bigDecimalCompositionTestCases()
		{
			Specification<BigDecimal> isPositive = value -> value != null && value.compareTo(BigDecimal.ZERO) > 0;
			Specification<BigDecimal> isGreaterThanHundred =
					value -> value != null && value.compareTo(BigDecimal.valueOf(100)) > 0;

			return Stream.of(
								 new BigDecimalTestCase(BigDecimal.valueOf(150), isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.AND, true,
										 "positive and greater than 100 should produce true result"),
								 new BigDecimalTestCase(BigDecimal.valueOf(50), isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.AND, false,
										 "positive and less than 100 should produce false result"),
								 new BigDecimalTestCase(BigDecimal.valueOf(-50), isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.AND, false,
										 "negative and less than 100 should produce false result"),
								 new BigDecimalTestCase(BigDecimal.valueOf(150), isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.OR, true,
										 "positive or greater than 100 should produce true result"),
								 new BigDecimalTestCase(BigDecimal.valueOf(-150), isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.OR, false,
										 "negative and less than -100 should produce false result"),
								 new BigDecimalTestCase(BigDecimal.valueOf(-50), isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.OR, false,
										 "negative or less than 100 should produce false result"),
								 new BigDecimalTestCase(BigDecimal.ZERO, isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.XOR, false,
										 "zero xor conditions should produce false result"),
								 new BigDecimalTestCase(null, isPositive, isGreaterThanHundred,
										 CompositeSpecification.CompositionType.AND, false,
										 "null value with AND should produce false result"))
						 .map(tc -> Arguments.of(tc.testValue, tc.leftSpec, tc.rightSpec, tc.compositionType,
								 tc.expectedResult, tc.description));
		}

		private record BigDecimalTestCase(BigDecimal testValue, Specification<BigDecimal> leftSpec,
										  Specification<BigDecimal> rightSpec,
										  CompositeSpecification.CompositionType compositionType,
										  boolean expectedResult, String description)
		{
		}
	}

	@Nested
	@DisplayName("Edge Cases and Boundary Tests")
	final class EdgeCasesAndBoundaryTests
	{
		@ParameterizedTest(name = "{5}")
		@MethodSource("edgeCaseTestCases")
		@DisplayName("should handle edge cases and boundary conditions correctly")
		void shouldHandleEdgeCasesAndBoundaryConditionsCorrectly(String testValue,
																 Specification<String> leftSpec,
																 Specification<String> rightSpec,
																 CompositeSpecification.CompositionType compositionType,
																 boolean expectedResult, String description)
		{
			var composite = CompositeSpecification.of(leftSpec, rightSpec, compositionType);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("Edge case should return %s for %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> edgeCaseTestCases()
		{
			var isNotEmpty = (Specification<String>) s -> s != null && !s.isEmpty();
			var hasMinLength = (Specification<String>) s -> s != null && s.length() >= 2;

			return Stream.of(
								 new EdgeCaseTestCase("", isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.AND,
										 false,
										 "empty string with AND should produce false result"),
								 new EdgeCaseTestCase("", isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.OR, false,
										 "empty string with OR should produce false result"),
								 new EdgeCaseTestCase("a", isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.AND,
										 false,
										 "single char and min length should produce false result"),
								 new EdgeCaseTestCase("a", isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.OR, true,
										 "single char or min length should produce true result"),
								 new EdgeCaseTestCase("ab", isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.AND,
										 true,
										 "min length boundary and not empty should produce true result"),
								 new EdgeCaseTestCase("   ", isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.AND,
										 true,
										 "whitespace string meeting both conditions should produce true result"),
								 new EdgeCaseTestCase(null, isNotEmpty, hasMinLength, CompositeSpecification.CompositionType.XOR,
										 false,
										 "null value with XOR should produce false result"))
						 .map(tc -> Arguments.of(tc.testValue, tc.leftSpec, tc.rightSpec, tc.compositionType,
								 tc.expectedResult, tc.description));
		}

		private record EdgeCaseTestCase(String testValue, Specification<String> leftSpec,
										Specification<String> rightSpec,
										CompositeSpecification.CompositionType compositionType, boolean expectedResult,
										String description)
		{
		}
	}

	@Nested
	@DisplayName("LocalDateTime Complex Composition Tests")
	final class LocalDateTimeComplexCompositionTests
	{
		@ParameterizedTest(name = "{5}")
		@MethodSource("localDateTimeComplexTestCases")
		@DisplayName("should handle complex LocalDateTime compositions correctly")
		void shouldHandleComplexLocalDateTimeCompositionsCorrectly(LocalDateTime testValue,
																   Specification<LocalDateTime> leftSpec,
																   Specification<LocalDateTime> rightSpec,
																   CompositeSpecification.CompositionType compositionType,
																   boolean expectedResult, String description)
		{
			var composite = CompositeSpecification.of(leftSpec, rightSpec, compositionType);

			var result = composite.isSatisfiedBy(testValue);

			assertThat(result).as("LocalDateTime composition should return %s for %s", expectedResult, description)
							  .isEqualTo(expectedResult);
		}

		private static Stream<Arguments> localDateTimeComplexTestCases()
		{
			var isBusinessHours = (Specification<LocalDateTime>) dt -> dt != null &&
					dt.getHour() >= 9 && dt.getHour() < 17;
			var isWeekday = (Specification<LocalDateTime>) dt -> dt != null &&
					dt.getDayOfWeek().getValue() <= 5;

			return Stream.of(new LocalDateTimeTestCase(LocalDateTime.of(2024, 1, 8, 10, 0), isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.AND, true,
										 "business hours and weekday should produce true result"),
								 new LocalDateTimeTestCase(LocalDateTime.of(2024, 1, 8, 8, 0), isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.AND, false,
										 "before business hours and weekday should produce false result"),
								 new LocalDateTimeTestCase(LocalDateTime.of(2024, 1, 6, 10, 0), isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.AND, false,
										 "business hours and weekend should produce false result"),
								 new LocalDateTimeTestCase(LocalDateTime.of(2024, 1, 6, 8, 0), isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.OR, false,
										 "before business hours or weekend should produce false result"),
								 new LocalDateTimeTestCase(LocalDateTime.of(2024, 1, 8, 18, 0), isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.OR, true,
										 "after business hours or weekday should produce true result"),
								 new LocalDateTimeTestCase(LocalDateTime.of(2024, 1, 8, 10, 0), isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.XOR, false,
										 "business hours xor weekday should produce false result"),
								 new LocalDateTimeTestCase(null, isBusinessHours, isWeekday,
										 CompositeSpecification.CompositionType.AND, false,
										 "null datetime with AND should produce false result"))
						 .map(tc -> Arguments.of(tc.testValue, tc.leftSpec, tc.rightSpec, tc.compositionType,
								 tc.expectedResult, tc.description));
		}

		private record LocalDateTimeTestCase(LocalDateTime testValue, Specification<LocalDateTime> leftSpec,
											 Specification<LocalDateTime> rightSpec,
											 CompositeSpecification.CompositionType compositionType,
											 boolean expectedResult, String description)
		{
		}
	}
}