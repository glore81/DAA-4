package suffix;

import java.util.Arrays;

public class KasaiLCP {
    public static int[] buildLCP(String s, int[] sa) {
        if (s == null || sa == null || s.isEmpty()) return new int[0];
        int n = s.length();
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int[] lcp = new int[n];
        int k = 0;

        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) {
                k = 0;
                continue;
            }
            int j = sa[rank[i] - 1];
            while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                k++;
            }
            lcp[rank[i]] = k;
            if (k > 0) k--;
        }
        return lcp;
    }

    public static int[] buildSuffixArraySimple(String s) {
        int n = s.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;

        Arrays.sort(order, (a, b) -> {
            String suffixA = s.substring(a);
            String suffixB = s.substring(b);
            return suffixA.compareTo(suffixB);
        });

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) sa[i] = order[i];
        return sa;
    }
}
