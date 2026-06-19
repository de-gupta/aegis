# Validation and Assessment / Decision

## Purpose

This note clarifies that we now have two different conceptual families and must not conflate them.

## 1. Validation Family

Validation is about intrinsic well-formedness.

Question:

- is this model/value/structure valid?

Current concepts:

- `Specification`
- `Violation`
- `Validation`
- `ValidationResult`
- `Validator`
- `ValidationPolicy`
- `ValidationOutcome`

Important property:

- validation evidence is rich
- final validation admission is still binary

Meaning:

- `ValidationResult` may contain many violations with severities
- `ValidationPolicy` collapses that evidence into accepted vs rejected

This is correct and should remain so.

## 2. Assessment / Decision Family

Assessment is a different concern.

Question:

- given this context, current state, source, actor, and possibly repository state, how should this operation be decided?

This is **not** validation.

It should therefore have its own parallel concepts.

Suggested family:

- `Assessment`
- `AssessmentResult`
- `DecisionPolicy`
- `Decision`

Alternative naming such as `OperationalAssessment` is acceptable in consumers, but the second lane should stay
assessment/decision-oriented and remain distinct from validation terminology.

## 3. Shared Concepts

These may stay shared between both families:

- `Specification`
- `Violation`
- severity
- accumulation/algebra of result evidence

Reason:

- both validation and assessment may produce rich violations/evidence

## 4. Separation Rule

Validation family:

- evaluates intrinsic correctness
- produces evidence
- applies a binary admission policy

Assessment / decision family:

- evaluates contextual permissibility of an operation
- may depend on actor, source, time, repository state, and current persisted model/graph state
- should not be forced into binary success/failure semantics
- should end in a decision rather than being described as validation

Examples of decisions:

- allow
- reject
- quarantine
- defer

## 5. Aegis Direction

`aegis` should remain generic and not take on Atlas-specific concepts such as node, graph, create attempt, or operation
context types.

It **can** be enriched to support a second generic family beside validation:

- contextual assessments
- rich evidence/result accumulation
- generic decision policies
- generic decisions

But:

- Atlas-specific operation inputs belong in Atlas
- concrete decision enums belong to consumers unless a truly generic abstraction emerges

## 6. Practical Design Guidance

Do not stretch `ValidationPolicy` to mean every kind of rule.

Instead:

- keep validation semantics intact
- add a distinct assessment / decision family
- let both families share violations/result evidence where useful

## Final Rule

Validation answers:

- is this thing well-formed?

Assessment / decision answers:

- given this situation, what is the decision for this operation here and now?

They are related, but they are not the same abstraction and should not share the same top-level terminology.