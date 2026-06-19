package de.gupta.validation.aegis.api.assessment.assessor;

import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;

@FunctionalInterface
public interface Assessor<T>
{
	AssessmentResult assess(T t);
}
