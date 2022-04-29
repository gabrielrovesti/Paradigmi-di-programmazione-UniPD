package it.unipd.pdp2021.ex1;

/**
 * An element of a program executed by a skater
 *
 * @param idx index of the element in the program
 * @param element element code
 * @param performance how the element has been executed, 0 not performed .. 10 perfect
 */
public record Element(int idx, String element) {

  /** This element code signals the end of the program. */
  public static final String END = "END";

  /** These elements are used to count the components' score. */
  public static final String SKILLS = "SKILLS";

  public static final String TRANSITIONS = "TRANSITIONS";
  public static final String PERFORMANCE = "PERFORMANCE";
  public static final String COMPOSITION = "COMPOSITION";
  public static final String INTERPRETATION = "INTERPRETATION";
}
;
