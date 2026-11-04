package matching;

import java.util.ArrayList;
import java.util.List;

public class RabinKarpMatcher {
    private static final int BASE = 31;
    private static final long MOD = 1_000_000_009L;

    public static MatchResult search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        long comparisons = 0;

        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return new MatchResult(matches, 0);
        }

        int n = text.length();
        int m = pattern.length();
        long patHash = 0;
        long curHash = 0;
        long h = 1;

        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % MOD;
        }

        for (int i = 0; i < m; i++) {
            patHash = (patHash * BASE + pattern.charAt(i)) % MOD;
            curHash = (curHash * BASE + text.charAt(i)) % MOD;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patHash == curHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    matches.add(i);
                }
            }

            if (i < n - m) {
                curHash = (BASE * (curHash - text.charAt(i) * h) + text.charAt(i + m)) % MOD;
                if (curHash < 0) {
                    curHash += MOD;
                }
            }
        }

        return new MatchResult(matches, comparisons);
    }
}
