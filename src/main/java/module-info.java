module de.gupta.validation.aegis
{
	requires de.gupta.aletheia;
	requires de.gupta.athena;

	exports de.gupta.validation.aegis.api.specification;
	exports de.gupta.validation.aegis.api.specification.collection;
	exports de.gupta.validation.aegis.api.specification.comparison;
	exports de.gupta.validation.aegis.api.specification.number;
	exports de.gupta.validation.aegis.api.specification.object;
	exports de.gupta.validation.aegis.api.specification.string;
	exports de.gupta.validation.aegis.api.specification.time;

	exports de.gupta.validation.aegis.api.validation;
	exports de.gupta.validation.aegis.api.validation.factories;
	exports de.gupta.validation.aegis.api.validation.result;

	exports de.gupta.validation.aegis.api.validator;

	exports de.gupta.validation.aegis.api.violation;
	exports de.gupta.validation.aegis.api.validation.outcome;
	exports de.gupta.validation.aegis.api.validation.factories.generic;
	exports de.gupta.validation.aegis.api.validation.policy;

	exports de.gupta.validation.aegis.api.policy;
}