package de.gupta.validation.aegis.api.policy;

@FunctionalInterface
public interface Policy<I, D>
{
	D apply(I input);
}