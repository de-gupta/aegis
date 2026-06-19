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

	exports de.gupta.validation.aegis.api.assessment.assessment;
	exports de.gupta.validation.aegis.api.assessment.assessment.decision;
	exports de.gupta.validation.aegis.api.assessment.assessment.factories.generic;
	exports de.gupta.validation.aegis.api.assessment.assessment.policy;
	exports de.gupta.validation.aegis.api.assessment.assessment.result;
	exports de.gupta.validation.aegis.api.assessment.assessor;

	exports de.gupta.validation.aegis.api.validation.validation;
	exports de.gupta.validation.aegis.api.validation.validation.factories;
	exports de.gupta.validation.aegis.api.validation.validation.factories.generic;
	exports de.gupta.validation.aegis.api.validation.validation.result;
	exports de.gupta.validation.aegis.api.validation.validation.outcome;
	exports de.gupta.validation.aegis.api.validation.validation.policy;

	exports de.gupta.validation.aegis.api.validation.validator;

	exports de.gupta.validation.aegis.api.violation;

	exports de.gupta.validation.aegis.api.policy;
}
