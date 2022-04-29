package it.unipd.pdp2021.ex1;

import java.util.List;
import java.util.Set;

/** Nation of the judge. */
public enum Nation {
  ITA(
      List.of(3, 3, 3, 2, 3, 3, -1, 3, 3, -1, 2, 3, 3),
      Set.of(7, 8, 10),
      9.75f,
      9.50f,
      9.75f,
      9.75f,
      10.00f),
  AUS(
      List.of(2, 2, 2, 2, 2, 1, -2, 1, 1, -2, 2, 2, 2),
      Set.of(6, 8),
      9.50f,
      9.25f,
      9.50f,
      9.50f,
      9.50f),
  RUS(
      List.of(3, 3, 3, 2, 3, 3, -2, 2, 2, -2, 2, 3, 2),
      Set.of(9, 10),
      9.75f,
      9.75f,
      9.75f,
      9.75f,
      9.75f),
  JPN(
      List.of(3, 3, 3, 2, 3, 3, -2, 3, 2, -1, 2, 3, 3),
      Set.of(6, 7, 8),
      9.75f,
      9.75f,
      9.75f,
      10.00f,
      9.75f),
  CHN(
      List.of(3, 3, 2, 2, 3, 2, -2, 2, 1, -2, 1, 3, 2),
      Set.of(7, 8, 9),
      9.50f,
      9.25f,
      9.00f,
      9.50f,
      9.75f),
  FRA(
      List.of(3, 3, 2, 2, 2, 2, -1, 2, 2, -2, 1, 2, 2),
      Set.of(7, 8, 9, 10),
      9.75f,
      9.50f,
      9.50f,
      9.75f,
      9.75f),
  GER(
      List.of(3, 3, 2, 2, 3, 3, -2, 3, 2, -1, 2, 3, 3),
      Set.of(6, 10),
      9.75f,
      9.50f,
      9.75f,
      9.50f,
      9.75f),
  KOR(
      List.of(3, 3, 2, 2, 3, 3, -2, 2, 2, -2, 3, 3, 2),
      Set.of(8, 9),
      9.75f,
      9.50f,
      9.75f,
      10.00f,
      9.75f),
  USA(
      List.of(3, 3, 2, 1, 3, 3, -1, 1, 1, -1, 2, 3, 2),
      Set.of(6),
      9.75f,
      9.50f,
      9.50f,
      9.75f,
      9.75f);

  public final List<Integer> goes;
  public final Set<Integer> review;
  public final float ss, tr, pf, cp, im;

  Nation(
      List<Integer> goes, Set<Integer> review, float ss, float tr, float pf, float cp, float im) {
    this.goes = goes;
    this.review = review;
    this.ss = ss;
    this.tr = tr;
    this.pf = pf;
    this.cp = cp;
    this.im = im;
  }
}
