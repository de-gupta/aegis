package de.gupta.validation.aegis.api.specification.comparison;

import de.gupta.validation.aegis.api.specification.Specification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CollectionComparisonSpecificationFactory")
final class CollectionComparisonSpecificationFactoryTest
{
	@FunctionalInterface
	private interface CollectionSpecificationFactoryInvocation
	{
		Specification<Collection<Integer>> create(Integer threshold, Comparator<Integer> comparator);
	}

	@Nested
	@DisplayName("for threshold comparisons")
	final class ForThresholdComparisons
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("evaluatesEveryElementAgainstTheThresholdCases")
		@DisplayName("evaluates every element against the threshold")
		void evaluatesEveryElementAgainstTheThreshold(final String as,
		                                              final CollectionSpecificationFactoryInvocation invocation,
		                                              final List<Integer> values, final boolean expected)
		{
			var specification = invocation.create(5, Comparator.naturalOrder());

			assertThat(specification.isSatisfiedBy(values))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> evaluatesEveryElementAgainstTheThresholdCases()
		{
			return Stream.of(
					Arguments.of("lessThan accepts collections whose elements are all smaller than the threshold",
							(CollectionSpecificationFactoryInvocation) CollectionComparisonSpecificationFactory::lessThan,
							List.of(1, 2, 3), true),
					Arguments.of("lessThanOrEqualTo accepts collections whose elements are all at most the threshold",
							(CollectionSpecificationFactoryInvocation) CollectionComparisonSpecificationFactory::lessThanOrEqualTo,
							List.of(4, 5), true),
					Arguments.of("equal accepts collections whose elements all equal the threshold",
							(CollectionSpecificationFactoryInvocation) CollectionComparisonSpecificationFactory::equal,
							List.of(5, 5), true),
					Arguments.of(
							"greaterThanOrEqualTo accepts collections whose elements are all at least the threshold",
							(CollectionSpecificationFactoryInvocation) CollectionComparisonSpecificationFactory::greaterThanOrEqualTo,
							List.of(5, 6), true),
					Arguments.of("greaterThan rejects collections containing the threshold value",
							(CollectionSpecificationFactoryInvocation) CollectionComparisonSpecificationFactory::greaterThan,
							List.of(5, 6), false)
			);
		}
	}

	@Nested
	@DisplayName("for notEqual()")
	final class ForNotEqual
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("rejectsOnlyCollectionsWhoseElementsAllEqualTheThresholdCases")
		@DisplayName("rejects only collections whose elements all equal the threshold")
		void rejectsOnlyCollectionsWhoseElementsAllEqualTheThreshold(final String as, final List<Integer> values,
		                                                             final boolean expected)
		{
			var specification =
					CollectionComparisonSpecificationFactory.notEqual(5, Comparator.naturalOrder());

			assertThat(specification.isSatisfiedBy(values))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> rejectsOnlyCollectionsWhoseElementsAllEqualTheThresholdCases()
		{
			return Stream.of(
					Arguments.of("collections with a different element are accepted", List.of(5, 6), true),
					Arguments.of("collections with only equal elements are rejected", List.of(5, 5), false)
			);
		}
	}
}