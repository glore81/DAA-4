import dp.EditDistance;
import dp.LongestCommonSubsequence;
import matching.KMPMatcher;
import matching.MatchResult;
import matching.NaiveMatcher;
import matching.RabinKarpMatcher;
import suffix.KasaiLCP;
import test.CorrectnessTests;

import java.util.Random;

public class BenchmarkMain {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final long SEED = 42L;
    private static final int RUNS = 5;

    public static void main(String[] args) {
        CorrectnessTests.runTests();

        Random rand = new Random(SEED);
        runDPBenchmark(rand);
        runStringMatchingBenchmark(rand);
        runKasaiBenchmark(rand);
    }

    private static String generateString(int length, Random rand) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private static void runDPBenchmark(Random rand) {
        int[] sizes = {100, 500, 1000};
        System.out.printf("%-10s | %-15s | %-15s%n", "Length (N)", "LCS Time (ms)", "Edit Dist (ms)");

        for (int size : sizes) {
            String s1 = generateString(size, rand);
            String s2 = generateString(size, rand);

            LongestCommonSubsequence.lcs(s1, s2);
            EditDistance.minDistance(s1, s2);

            long totalLcs = 0;
            for (int k = 0; k < RUNS; k++) {
                long t0 = System.nanoTime();
                LongestCommonSubsequence.lcs(s1, s2);
                totalLcs += (System.nanoTime() - t0);
            }

            long totalEdit = 0;
            for (int k = 0; k < RUNS; k++) {
                long t0 = System.nanoTime();
                EditDistance.minDistance(s1, s2);
                totalEdit += (System.nanoTime() - t0);
            }

            System.out.printf("%-10d | %-15.3f | %-15.3f%n", size, (totalLcs / (double) RUNS) / 1e6, (totalEdit / (double) RUNS) / 1e6);
        }
        System.out.println();
    }

    private static void runStringMatchingBenchmark(Random rand) {
        int[] textSizes = {10000, 50000, 100000};
        int patLen = 20;

        System.out.printf("%-10s | %-8s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s%n",
                "Text Len", "Pattern", "Naive(ms)", "N_Comp", "KMP(ms)", "KMP_Comp", "RK(ms)", "RK_Comp");

        for (int n : textSizes) {
            String text = generateString(n, rand);
            int mid = n / 2;
            String patPresent = text.substring(mid, mid + patLen);
            String patAbsent = "zzzzzzzzzzzzzzzzzzzz";

            String[] testPats = {patPresent, patAbsent};
            String[] types = {"Present", "Absent"};

            for (int pIdx = 0; pIdx < 2; pIdx++) {
                String pat = testPats[pIdx];

                NaiveMatcher.search(text, pat);
                KMPMatcher.search(text, pat);
                RabinKarpMatcher.search(text, pat);

                long totalNaive = 0, totalKmp = 0, totalRk = 0;
                long compNaive = 0, compKmp = 0, compRk = 0;

                for (int k = 0; k < RUNS; k++) {
                    long t0 = System.nanoTime();
                    MatchResult resN = NaiveMatcher.search(text, pat);
                    totalNaive += (System.nanoTime() - t0);
                    compNaive = resN.comparisons;

                    long t1 = System.nanoTime();
                    MatchResult resK = KMPMatcher.search(text, pat);
                    totalKmp += (System.nanoTime() - t1);
                    compKmp = resK.comparisons;

                    long t2 = System.nanoTime();
                    MatchResult resR = RabinKarpMatcher.search(text, pat);
                    totalRk += (System.nanoTime() - t2);
                    compRk = resR.comparisons;
                }

                System.out.printf("%-10d | %-8s | %-10.3f | %-10d | %-10.3f | %-10d | %-10.3f | %-10d%n",
                        n, types[pIdx],
                        (totalNaive / (double) RUNS) / 1e6, compNaive,
                        (totalKmp / (double) RUNS) / 1e6, compKmp,
                        (totalRk / (double) RUNS) / 1e6, compRk);
            }
        }
        System.out.println();
    }

    private static void runKasaiBenchmark(Random rand) {
        int[] sizes = {1000, 5000, 10000};
        System.out.printf("%-12s | %-15s%n", "String Size", "Kasai Time (ms)");

        for (int size : sizes) {
            String s = generateString(size, rand);
            int[] sa = KasaiLCP.buildSuffixArraySimple(s);

            KasaiLCP.buildLCP(s, sa);

            long totalKasai = 0;
            for (int k = 0; k < RUNS; k++) {
                long t0 = System.nanoTime();
                KasaiLCP.buildLCP(s, sa);
                totalKasai += (System.nanoTime() - t0);
            }
            System.out.printf("%-12d | %-15.3f%n", size, (totalKasai / (double) RUNS) / 1e6);
        }
    }
}
