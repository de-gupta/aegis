package de.gupta.validation.aegis.api.violation;

public interface Violation
{
	String message();

	Severity severity();
}