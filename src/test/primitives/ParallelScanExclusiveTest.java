package primitives;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParallelScanExclusiveTest {
    @Test
    public void parallelScan() {
        var block = 3;
        var values = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        var expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var tree = ParallelScanExclusive.tree(values, block);

        var result = new ParallelScanExclusive(block, tree, 0, values.length, values).compute();

        assertThat(values).isEqualTo(expected);
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void parallelScanBlockSizeOne() {
        var block = 1;
        var values = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        var expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var tree = ParallelScanExclusive.tree(values, block);

        var result = new ParallelScanExclusive(block, tree, 0, values.length, values).compute();

        assertThat(values).isEqualTo(expected);
        assertThat(result).isEqualTo(10);
    }
}
