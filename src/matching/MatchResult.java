package matching;

import java.util.List;

public class MatchResult {
    public final List<Integer> indices;
    public final long comparisons;

    public MatchResult(List<Integer> indices, long comparisons) {
        this.indices = indices;
        this.comparisons = comparisons;
    }
}
