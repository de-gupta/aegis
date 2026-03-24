package de.gupta.validation.aegis.api.specification.comparison;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class ComparisonSpecificationFactoryTest
{
	@Test
	@DisplayName("notEqual should return false when the value is null")
	void shouldReturnFalseForNotEqualWhenValueIsNull()
	{
		assertThat(ComparisonSpecificationFactory.notEqual(10, Comparator.naturalOrder())
												 .isSatisfiedBy(null)).isFalse();
	}

	@Test
	@DisplayName("notEqual should return false when the threshold is null")
	void shouldReturnFalseForNotEqualWhenThresholdIsNull()
	{
		assertThat(ComparisonSpecificationFactory.notEqual(null, Comparator.<Integer>naturalOrder())
												 .isSatisfiedBy(10)).isFalse();
	}

	@Test
	@DisplayName("equal should return false instead of throwing when the value is null")
	void shouldReturnFalseForEqualWhenValueIsNull()
	{
		assertThat(ComparisonSpecificationFactory.equal(10, Comparator.naturalOrder())
												 .isSatisfiedBy(null)).isFalse();
	}

	@Test
	@DisplayName("collection equal should return false instead of throwing when the collection is null")
	void shouldReturnFalseForCollectionEqualWhenCollectionIsNull()
	{
		assertThat(CollectionComparisonSpecificationFactory.equal(10, Comparator.naturalOrder())
														   .isSatisfiedBy(null)).isFalse();
	}

	@Test
	@DisplayName("collection notEqual should return false when the threshold is null")
	void shouldReturnFalseForCollectionNotEqualWhenThresholdIsNull()
	{
		assertThat(CollectionComparisonSpecificationFactory.notEqual(null, Comparator.<Integer>naturalOrder())
														   .isSatisfiedBy(List.of(1, 2, 3))).isFalse();
	}
}