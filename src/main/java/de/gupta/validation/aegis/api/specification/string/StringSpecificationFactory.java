package de.gupta.validation.aegis.api.specification.string;

import de.gupta.commons.utility.string.StringSanitizationUtility;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.specification.SpecificationFactory;

public final class StringSpecificationFactory
{
	public static Specification<String> notBlank()
	{
		return SpecificationFactory.from(StringSanitizationUtility::isNotBlank);
	}

	public static Specification<String> trimmed()
	{
		return SpecificationFactory.from(StringSanitizationUtility::isTrimmed);
	}

	private StringSpecificationFactory()
	{
	}
}