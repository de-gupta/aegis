package de.gupta.validation.aegis.api.specification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

final class SpecificationFactoryTest
{
	private record Person(String name, int age)
	{
	}

	@Nested
	@DisplayName("Predicate check")
	final class PredicateFactoryTests
	{
		@DisplayName("should create specification from predicate")
		@ParameterizedTest(name = "{0}")
		@MethodSource({
				"numericPredicates",
				"stringPredicates",
				"collectionPredicates",
				"dateTimePredicates",
				"customObjectPredicates",
				"arcanePredicates",
				"edgeCasePredicates"
		})
		<T> void shouldCreateSpecificationFromPredicate(final String description, final Predicate<T> predicate,
														final T value, final boolean expectedResult)
		{
			assertThat(SpecificationFactory.from(predicate).isSatisfiedBy(value))
					.as("Specification %s should return %s for the value %s", description, expectedResult, value)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> numericPredicates()
		{
			return Stream.of(
					PredicateTestCase.of("isEven", (Integer x) -> x % 2 == 0, 2, true),
					PredicateTestCase.of("isEven", (Integer x) -> x % 2 == 0, 3, false),
					PredicateTestCase.of("isOdd", (Integer x) -> x % 2 != 0, 3, true),
					PredicateTestCase.of("isOdd", (Integer x) -> x % 2 != 0, 2, false),
					PredicateTestCase.of("isPositive", (Integer x) -> x > 0, 1, true),
					PredicateTestCase.of("isPositive", (Integer x) -> x > 0, -1, false),
					PredicateTestCase.of("isPrime", (Integer n) ->
					{
						if (n < 2) return false;
						for (int i = 2; i <= Math.sqrt(n); i++)
						{
							if (n % i == 0) return false;
						}
						return true;
					}, 17, true),
					PredicateTestCase.of("isPrime", (Integer n) ->
					{
						if (n < 2) return false;
						for (int i = 2; i <= Math.sqrt(n); i++)
						{
							if (n % i == 0) return false;
						}
						return true;
					}, 16, false)
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private static Stream<Arguments> stringPredicates()
		{
			return Stream.of(
					PredicateTestCase.of("isEmpty", String::isEmpty, "", true),
					PredicateTestCase.of("isEmpty", String::isEmpty, "hello", false),
					PredicateTestCase.of("isBlank", (String s) -> s.trim().isEmpty(), "   ", true),
					PredicateTestCase.of("isBlank", (String s) -> s.trim().isEmpty(), " hello ", false),
					PredicateTestCase.of("containsDigit", (String s) -> s.matches(".*\\d.*"), "abc123", true),
					PredicateTestCase.of("containsDigit", (String s) -> s.matches(".*\\d.*"), "abcdef", false),
					PredicateTestCase.of("isValidEmail", (String s) -> s.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"),
							"user@example.com", true),
					PredicateTestCase.of("isValidEmail", (String s) -> s.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"),
							"invalid.email", false),
					PredicateTestCase.of("startsWithUppercase",
							(String s) -> !s.isEmpty() && Character.isUpperCase(s.charAt(0)),
							"Hello", true),
					PredicateTestCase.of("startsWithUppercase",
							(String s) -> !s.isEmpty() && Character.isUpperCase(s.charAt(0)),
							"hello", false)
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private static Stream<Arguments> collectionPredicates()
		{
			return Stream.of(
					PredicateTestCase.of("listIsEmpty", (List<String> list) -> list.isEmpty(),
							Collections.emptyList(), true),
					PredicateTestCase.of("listIsEmpty", List::isEmpty,
							List.of("item"), false),
					PredicateTestCase.of("listHasMoreThanThreeItems", (List<Integer> list) -> list.size() > 3,
							List.of(1, 2, 3, 4, 5), true),
					PredicateTestCase.of("listHasMoreThanThreeItems", (List<Integer> list) -> list.size() > 3,
							List.of(1, 2), false),
					PredicateTestCase.of("setContainsDuplicateLength",
							(Set<String> set) -> set.stream().mapToInt(String::length).distinct().count() < set.size(),
							Set.of("cat", "dog", "bat"), true),
					PredicateTestCase.of("setContainsDuplicateLength",
							(Set<String> set) -> set.stream().mapToInt(String::length).distinct().count() < set.size(),
							Set.of("cat", "bird", "elephant"), false),
					PredicateTestCase.of("mapHasEvenNumberOfEntries", (Map<String, Integer> map) -> map.size() % 2 == 0,
							Map.of("a", 1, "b", 2), true),
					PredicateTestCase.of("mapHasEvenNumberOfEntries", (Map<String, Integer> map) -> map.size() % 2 == 0,
							Map.of("a", 1, "b", 2, "c", 3), false)
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private static Stream<Arguments> dateTimePredicates()
		{
			return Stream.of(
					PredicateTestCase.of("isWeekend", (LocalDate date) ->
									date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY,
							LocalDate.of(2024, 1, 6), true), // Saturday
					PredicateTestCase.of("isWeekend", (LocalDate date) ->
									date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY,
							LocalDate.of(2024, 1, 8), false), // Monday
					PredicateTestCase.of("isInPast", (LocalDateTime dt) -> dt.isBefore(LocalDateTime.now()),
							LocalDateTime.of(2020, 1, 1, 0, 0), true),
					PredicateTestCase.of("isLeapYear", Year::isLeap,
							Year.of(2024), true),
					PredicateTestCase.of("isLeapYear", Year::isLeap,
							Year.of(2023), false),
					PredicateTestCase.of("isFirstDayOfMonth", (LocalDate date) -> date.getDayOfMonth() == 1,
							LocalDate.of(2024, 3, 1), true),
					PredicateTestCase.of("isFirstDayOfMonth", (LocalDate date) -> date.getDayOfMonth() == 1,
							LocalDate.of(2024, 3, 15), false)
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private static Stream<Arguments> customObjectPredicates()
		{
			return Stream.of(
					PredicateTestCase.of("personIsAdult", (Person p) -> p.age() >= 18,
							new Person("John", 25), true),
					PredicateTestCase.of("personIsAdult", (Person p) -> p.age() >= 18,
							new Person("Jane", 16), false),
					PredicateTestCase.of("personHasLongName", (Person p) -> p.name().length() > 10,
							new Person("Christopher", 30), true),
					PredicateTestCase.of("personHasLongName", (Person p) -> p.name().length() > 10,
							new Person("Bob", 25), false),
					PredicateTestCase.of("personNameStartsWithVowel",
							(Person p) -> "AEIOUaeiou".contains(p.name().substring(0, 1)),
							new Person("Alice", 30), true),
					PredicateTestCase.of("personNameStartsWithVowel",
							(Person p) -> "AEIOUaeiou".contains(p.name().substring(0, 1)),
							new Person("Bob", 25), false)
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private static Stream<Arguments> arcanePredicates()
		{
			return Stream.of(
					PredicateTestCase.of("isPerfectSquare", (Integer n) ->
					{
						if (n < 0) return false;
						int sqrt = (int) Math.sqrt(n);
						return sqrt * sqrt == n;
					}, 16, true),
					PredicateTestCase.of("isPerfectSquare", (Integer n) ->
					{
						if (n < 0) return false;
						int sqrt = (int) Math.sqrt(n);
						return sqrt * sqrt == n;
					}, 15, false),

					PredicateTestCase.of("isPalindrome", (String s) ->
					{
						String cleaned = s.replaceAll("\\W", "").toLowerCase();
						return cleaned.contentEquals(new StringBuilder(cleaned).reverse());
					}, "A man a plan a canal Panama", true),
					PredicateTestCase.of("isPalindrome", (String s) ->
					{
						String cleaned = s.replaceAll("\\W", "").toLowerCase();
						return cleaned.contentEquals(new StringBuilder(cleaned).reverse());
					}, "hello world", false),

					PredicateTestCase.of("hasBalancedParentheses", (String s) ->
					{
						int count = 0;
						for (char c : s.toCharArray())
						{
							if (c == '(') count++;
							else if (c == ')') count--;
							if (count < 0) return false;
						}
						return count == 0;
					}, "((hello) world)", true),
					PredicateTestCase.of("hasBalancedParentheses", (String s) ->
					{
						int count = 0;
						for (char c : s.toCharArray())
						{
							if (c == '(') count++;
							else if (c == ')') count--;
							if (count < 0) return false;
						}
						return count == 0;
					}, "((hello) world", false),

					PredicateTestCase.of("satisfiesLuhnAlgorithm", (String cardNumber) ->
					{
						if (!cardNumber.matches("\\d+")) return false;
						int sum = 0;
						boolean alternate = false;
						for (int i = cardNumber.length() - 1; i >= 0; i--)
						{
							int digit = Character.getNumericValue(cardNumber.charAt(i));
							if (alternate)
							{
								digit *= 2;
								if (digit > 9) digit = digit / 10 + digit % 10;
							}
							sum += digit;
							alternate = !alternate;
						}
						return sum % 10 == 0;
					}, "4532015112830366", true), // Valid credit card number
					PredicateTestCase.of("satisfiesLuhnAlgorithm", (String cardNumber) ->
					{
						if (!cardNumber.matches("\\d+")) return false;
						int sum = 0;
						boolean alternate = false;
						for (int i = cardNumber.length() - 1; i >= 0; i--)
						{
							int digit = Character.getNumericValue(cardNumber.charAt(i));
							if (alternate)
							{
								digit *= 2;
								if (digit > 9) digit = digit / 10 + digit % 10;
							}
							sum += digit;
							alternate = !alternate;
						}
						return sum % 10 == 0;
					}, "1234567890123456", false) // Invalid credit card number
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private static Stream<Arguments> edgeCasePredicates()
		{
			return Stream.of(
					PredicateTestCase.of("alwaysTrue", (Object _) -> true, "anything", true),
					PredicateTestCase.of("alwaysFalse", (Object _) -> false, "anything", false),
					PredicateTestCase.of("isNull", Objects::isNull, null, true),
					PredicateTestCase.of("isNull", Objects::isNull, "not null", false),
					PredicateTestCase.of("isNotNull", Objects::nonNull, "not null", true),
					PredicateTestCase.of("isNotNull", Objects::nonNull, null, false),
					PredicateTestCase.of("stringEqualsItself", (String s) -> s.equals(s), "test", true),
					PredicateTestCase.of("integerEqualsZero", (Integer i) -> i.equals(0), 0, true),
					PredicateTestCase.of("integerEqualsZero", (Integer i) -> i.equals(0), 42, false)
			).map(tc -> Arguments.of(tc.description, tc.predicate, tc.value, tc.expectedResult));
		}

		private record PredicateTestCase<T>(String description, Predicate<T> predicate, T value, boolean expectedResult)
		{
			static <T> PredicateTestCase<T> of(final String description, final Predicate<T> predicate, final T value,
											   final boolean expectedResult)
			{
				return new PredicateTestCase<>(description, predicate, value, expectedResult);
			}
		}
	}
}