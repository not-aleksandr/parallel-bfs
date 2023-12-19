package bfs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelBfsTest {

    @Test
    public void parallelBfs() {
        int[][] graph = new int[][]{
                {1, 2, 3},
                {0, 3},
                {0, 4, 5},
                {0, 1},
                {2, 5},
                {2, 4}
        };
        var expectedDist = new int[]{0, 1, 1, 1, 2, 2};

        var result = new ParallelBfs(3, graph).compute();

        assertThat(result).isEqualTo(expectedDist);
    }

    @Test
    public void parallelBfsResultEqualToSequential() {
        int block = 10;
        int[] n = {5, 10, 20, 50, 100, 200, 1000};
        int[] m = {5, 15, 100, 700, 2500, 15000, 400000};
        for (int i = 0; i < n.length; i++) {
            int[][] graph = randomConnectedGraph(n[i], m[i]);
            var resultParallel = new ParallelBfs(block, graph).compute();
            var resultSeq = SequentialBfs.bfs(graph);
            assertThat(resultParallel).isEqualTo(resultSeq);
        }
    }


    private int[][] randomConnectedGraph(int n, int m) {
        Random random = new Random(42);
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        if (m < n - 1) throw new IllegalStateException("Edge count `m` is less than `n - 1`");
        ArrayList<Set<Integer>> components = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Set<Integer> component = new HashSet<>();
            component.add(i);
            components.add(component);
        }
        Set<Edge> edges = new HashSet<>();
        int edgeCount = 0;
        while (edgeCount < n - 1) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            int u_component_i = -1;
            int v_component_i = -1;
            for (int i = 0; i < n; i++) {
                Set<Integer> component = components.get(i);
                if (component.contains(u)) u_component_i = i;
                if (component.contains(v)) v_component_i = i;
            }
            if (u_component_i == v_component_i) continue;
            Set<Integer> u_component = components.get(u_component_i);
            Set<Integer> v_component = components.get(v_component_i);
            if (u_component.size() < v_component.size()) {
                v_component.addAll(u_component);
                components.set(u_component_i, new HashSet<>());
            } else {
                u_component.addAll(v_component);
                components.set(v_component_i, new HashSet<>());
            }
            graph.get(u).add(v);
            graph.get(v).add(u);
            edges.add(new Edge(u, v));
            edges.add(new Edge(v, u));
            edgeCount++;
        }
        while (edgeCount < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u == v) continue;
            Edge edge = new Edge(u, v);
            if (edges.contains(edge)) continue;
            graph.get(u).add(v);
            graph.get(v).add(u);
            edges.add(edge);
            edges.add(new Edge(v, u));
            edgeCount++;
        }
        return graph.stream().map(neighbors -> neighbors.stream().mapToInt(v -> v).toArray()).toArray(int[][]::new);
    }

    private record Edge(int u, int v) {
    }

}