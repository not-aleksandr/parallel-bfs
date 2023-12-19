package bfs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SequentialBfsTest {
    @Test
    public void bfs1() {
        int[][] graph = new int[][]{
                {1, 2, 3},
                {0, 3},
                {0, 4, 5},
                {0, 1},
                {2, 5},
                {2, 4}
        };
        var expectedDist = new int[]{0, 1, 1, 1, 2, 2};

        var result = SequentialBfs.bfs(graph);

        assertThat(result).isEqualTo(expectedDist);
    }
}