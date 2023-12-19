package primitives;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelForTest {

    @Test
    public void parallelFor() {
        var values = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        new ParallelFor(3, 0, 10, i -> values[i]++).compute();

        assertThat(values).isEqualTo(expected);
    }

    @Test
    public void parallelForBlockSizeOne() {
        var values = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        new ParallelFor(1, 0, 10, i -> values[i]++).compute();

        assertThat(values).isEqualTo(expected);
    }

}