import bfs.ParallelBfs;
import bfs.SequentialBfs;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

public class Benchmark {
    private static final int GRAPH_SIZE = 490;
    private static final ForkJoinPool POOL = new ForkJoinPool(getRuntime().availableProcessors());
    private static final int[] BLOCKS = new int[]{2000};
    private static final int WARMUP_ITERATIONS = 3;
    private static final int MEASURE_ITERATIONS = 5;

    public static void main(String[] arguments) {
        var availableProcessors = getRuntime().availableProcessors();
        var graph = cubicGraph();
        logSeparator();
        log("CPU count", availableProcessors);
        logSeparator();

        log("Warmup iterations", WARMUP_ITERATIONS);
        run(BFSType.SEQUENTIAL_BFS, WARMUP_ITERATIONS, graph);
        run(BFSType.PARALLEL_BFS, WARMUP_ITERATIONS, graph);
        logSeparator();

        log("Measure iterations", MEASURE_ITERATIONS);
        run(BFSType.SEQUENTIAL_BFS, MEASURE_ITERATIONS, graph);
        run(BFSType.PARALLEL_BFS, MEASURE_ITERATIONS, graph);
        logSeparator();
    }

    private static void run(BFSType bfsType, int iterationCount, int[][] graph) {
        log("BFS with type", bfsType);
        var blocks = bfsType.requireBlock ? BLOCKS : new int[1];
        for (int block : blocks) {
            if (bfsType.requireBlock) {
                log("Block size", block);
            }
            var bfsRunner = bfsRunner(bfsType, block);
            double totalTime = 0;
            for (int i = 0; i < iterationCount; i++) {
                System.gc();
                var start = currentTimeMillis();
                bfsRunner.accept(graph);

                var end = currentTimeMillis();
                long time = end - start;
                log(format("Iteration %d", i + 1), format("%d millis", time));
                totalTime += time;
            }
            log("Avg time", format("%f millis", totalTime / iterationCount));
        }
    }

    private static void log(String header, Object value) {
        System.out.print(header);
        System.out.print(":     ");
        System.out.println(value.toString());
    }

    private static void logSeparator() {
        System.out.println("=".repeat(10));
    }

    private static Consumer<int[][]> bfsRunner(BFSType type, int block) {
        return switch (type) {
            case SEQUENTIAL_BFS -> SequentialBfs::bfs;
            case PARALLEL_BFS -> (graph) -> POOL.invoke(new ParallelBfs(block, graph));
        };
    }

    private enum BFSType {
        SEQUENTIAL_BFS(false),
        PARALLEL_BFS(true);

        final boolean requireBlock;

        BFSType(boolean requireBlock) {
            this.requireBlock = requireBlock;
        }
    }

    public static int[][] cubicGraph() {
        int[][] cubicGraph = new int[GRAPH_SIZE * GRAPH_SIZE * GRAPH_SIZE][];
        for (int i = 0; i < GRAPH_SIZE; i++) {
            for (int j = 0; j < GRAPH_SIZE; j++) {
                for (int k = 0; k < GRAPH_SIZE; k++) {
                    var neighbours = new ArrayList<Integer>();
                    if (i != 0) {
                        neighbours.add(vertex(i - 1, j, k));
                    }
                    if (j != 0) {
                        neighbours.add(vertex(i, j - 1, k));
                    }
                    if (k != 0) {
                        neighbours.add(vertex(i, j, k - 1));
                    }
                    if (i != GRAPH_SIZE - 1) {
                        neighbours.add(vertex(i + 1, j, k));
                    }
                    if (j != GRAPH_SIZE - 1) {
                        neighbours.add(vertex(i, j + 1, k));
                    }
                    if (k != GRAPH_SIZE - 1) {
                        neighbours.add(vertex(i, j, k + 1));
                    }
                    cubicGraph[vertex(i, j, k)] = neighbours.stream().mapToInt(v -> v).toArray();

                }
            }
        }
        return cubicGraph;
    }

    private static int vertex(int i, int j, int k) {
        return i * GRAPH_SIZE * GRAPH_SIZE + j * GRAPH_SIZE + k;
    }
}
