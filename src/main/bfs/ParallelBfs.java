package bfs;

import primitives.ParallelFor;
import primitives.ParallelScanExclusive;

import java.util.concurrent.RecursiveTask;

public class ParallelBfs extends RecursiveTask<int[]> {
    public static final int INITIAL_DIST = 0;
    private final int block;
    private final int[][] graph;
    private final int[] frontier;
    private int frontierSize;
    private final int[] shifts;
    private int[] newFrontier;
    private int[] scanTree;
    private final int[] dist;
    private final int[] newFrontierPosition;

    public ParallelBfs(int block, int[][] graph) {
        this.block = block;
        this.graph = graph;
        this.shifts = new int[graph.length];
        this.frontier = new int[graph.length];
        this.newFrontier = new int[graph.length];
        this.scanTree = ParallelScanExclusive.tree(newFrontier, block);
        this.dist = new int[graph.length];
        this.newFrontierPosition = new int[graph.length];
    }

    @Override
    protected int[] compute() {
        frontier[0] = 0;
        frontierSize = 1;
        dist[0] = -1;
        int nextDist = 1;
        while (frontierSize > 0) {
            int finalNextDist = nextDist;
            int newFrontierBound = calculateShifts();
            ensureNewFrontierSizeCapacity(newFrontierBound);
            new ParallelFor(block, 0, frontierSize, i -> {
                int u = frontier[i];
                int shift = shifts[i];

                for (int j = 0; j < graph[u].length; j++) {
                    int v = graph[u][j];
                    if (dist[v] == INITIAL_DIST) {
                        dist[v] = finalNextDist;
                        int frontierPosition = shift + j;
                        newFrontierPosition[v] = frontierPosition;
                        newFrontier[frontierPosition] = v;
                    }
                }
            }).compute();
            filterFrontier(newFrontierBound);
            nextDist++;
        }
        dist[0] = 0;
        return dist;
    }

    private int calculateShifts() {
        new ParallelFor(block, 0, frontierSize, i -> shifts[i] = graph[frontier[i]].length).compute();
        return new ParallelScanExclusive(block, scanTree, 0, frontierSize, shifts).compute();
    }

    private void filterFrontier(int newFrontierBound) {
        frontierSize = 0;
        for (int i = 0; i < newFrontierBound; i++) {
            if (newFrontier[i] != 0 && newFrontierPosition[newFrontier[i]] == i) {
                frontier[frontierSize++] = newFrontier[i];
            }
            newFrontier[i] = 0;
        }
    }

    private void ensureNewFrontierSizeCapacity(int newFrontierBound) {
        if (newFrontierBound > newFrontier.length) {
            newFrontier = new int[newFrontierBound];
            scanTree = ParallelScanExclusive.tree(newFrontier, block);
        }
    }
}
