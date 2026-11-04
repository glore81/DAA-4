package test;

import dp.EditDistance;
import dp.LongestCommonSubsequence;
import matching.KMPMatcher;
import matching.MatchResult;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import suffix.KasaiLCP;

import java.util.Arrays;
import java.util.List;

public class CorrectnessTests {

    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        testLCS();
        testEditDistance();
        testStringMatching();
        testKasai();
        System.out.println("All correctness tests passed successfully.\n");
    }

    private static void testLCS() {
        assertEq(5, LongestCommonSubsequence.lcs("HELLO", "HELLO"));
        assertEq(0, LongestCommonSubsequence.lcs("ABC", "XYZ"));
        assertEq(0, LongestCommonSubsequence.lcs("", "ABC"));
        assertEq(3, LongestCommonSubsequence.lcs("abcde", "ace"));
    }

    private static void testEditDistance() {
        assertEq(0, EditDistance.minDistance("kitten", "kitten"));
        assertEq(5, EditDistance.minDistance("", "apple"));
        assertEq(1, EditDistance.minDistance("cat", "cut"));
        assertEq(1, EditDistance.minDistance("cat", "cats"));
        assertEq(1, EditDistance.minDistance("cats", "cat"));
    }

    private static void testStringMatching() {
        String text = "ABABDABACDABABCABAB";
        String[] patterns = {
                "AB",
                "ABABD",
                "CDABA",
                "CABAB",
                "XYZ",
                "ABABDABACDABABCABABXYZ",
                ""
        };

        for (String pat : patterns) {
            MatchResult naive = NaiveMatcher.search(text, pat);
            MatchResult kmp = KMPMatcher.search(text, pat);
            MatchResult rk = RabinKarpMatcher.search(text, pat);

            assertEq(naive.indices, kmp.indices);
            assertEq(naive.indices, rk.indices);
        }
    }

    private static void testKasai() {
        String s = "banana";
        int[] sa = KasaiLCP.buildSuffixArraySimple(s);
        int[] expectedSA = {5, 3, 1, 0, 4, 2};
        if (!Arrays.equals(sa, expectedSA)) throw new RuntimeException("Kasai SA mismatch");

        int[] lcp = KasaiLCP.buildLCP(s, sa);
        int[] expectedLCP = {0, 1, 3, 0, 0, 2};
        if (!Arrays.equals(lcp, expectedLCP)) throw new RuntimeException("Kasai LCP mismatch");
    }

    private static void assertEq(int expected, int actual) {
        if (expected != actual) throw new RuntimeException("Expected: " + expected + ", Got: " + actual);
    }

    private static void assertEq(List<Integer> expected, List<Integer> actual) {
        if (!expected.equals(actual)) throw new RuntimeException("Expected: " + expected + ", Got: " + actual);
    }

}
