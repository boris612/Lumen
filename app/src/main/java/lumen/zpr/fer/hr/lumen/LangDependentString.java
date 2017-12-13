package lumen.zpr.fer.hr.lumen;

/**
 * Created by Alen on 13.12.2017..
 *
 * Predstavlja String specijaliziran za rijeci nekog jezika, tj. omogucuje npr. u hrv. jeziku prepoznavanje dvoznakovnih slova kao jedno slovo.
 */

public interface LangDependentString {

    int length();
    String charAt(int i);
    LangDependentString toLowerCase();
    LangDependentString toUpperCase();
}
