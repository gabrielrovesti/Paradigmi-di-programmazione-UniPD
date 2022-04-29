package it.unipd.pdp2021.ex1;

/** A vote grades part of an element */
public interface Vote {
  float value();
}

/** A vote emitted by a judge (i.e. not a Base Value) */
interface JudgeVote extends Vote {
  Judge judge();
}

/** Base value of an element */
record BaseValue(Element element, float value) implements Vote {}

/** GOE of an element by a judge */
record GradeOfExecution(Element element, Judge judge, float value, boolean review)
    implements JudgeVote {}

/** Skating skills component by a judge */
record SkatingSkills(Judge judge, float value) implements JudgeVote {}

/** Transitions component by a judge */
record Transitions(Judge judge, float value) implements JudgeVote {}

/** Performance component by a judge */
record Performance(Judge judge, float value) implements JudgeVote {}

/** Composition component by a judge */
record Composition(Judge judge, float value) implements JudgeVote {}

/** Interpretation of Music component by a judge */
record Interpretation(Judge judge, float value) implements JudgeVote {}
