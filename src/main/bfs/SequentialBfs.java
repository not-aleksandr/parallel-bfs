package bfs;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.Arrays.stream;

public class SequentialBfs {

    public static int[] bfs(int[][] graph) {
        int[] dist = new int[graph.length];
        boolean[] used = new boolean[graph.length];
        Queue<Integer> nextV = new ArrayDeque<>();
        nextV.add(0);
        used[0] = true;
        while (!nextV.isEmpty()) {
            int v = nextV.poll();
            stream(graph[v]).filter(u -> !used[u]).forEach(u -> {
                dist[u] = dist[v] + 1;
                used[u] = true;
                nextV.add(u);
            });
        }
        return dist;
    }
}
