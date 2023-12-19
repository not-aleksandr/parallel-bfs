package primitives;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ParallelScanExclusive extends RecursiveTask<Integer> {
    private final int block;
    private final int[] array;
    private final int[] tree;
    private final int from;
    private final int to;

    public ParallelScanExclusive(int block, int[] tree, int from, int to, int[] array) {
        this.block = block;
        this.array = array;
        this.tree = tree;
        this.from = from;
        this.to = to;
    }

    @Override
    public Integer compute() {
        new Build(from, to, 0).compute();
        new Calculate(from, to, 0, 0).compute();
        return tree[0];
    }

    public static int[] tree(int[] array, int block) {
        var size = (array.length + block - 1) / block;
        size *= 4;
        return new int[size];
    }

    private class Build extends RecursiveAction {
        private final int from;
        private final int to;
        private final int v;

        private Build(int from, int to, int v) {
            this.from = from;
            this.to = to;
            this.v = v;
        }

        @Override
        protected void compute() {
            if (to - from <= block) {
                int sum = 0;
                for (int i = from; i < to; i++) sum += array[i];
                tree[v] = sum;
                return;
            }
            int middle = (from + to) / 2;
            int left = 2 * v + 1;
            int right = 2 * v + 2;
            invokeAll(new Build(from, middle, left), new Build(middle, to, right));
            tree[v] = tree[left] + tree[right];
        }
    }

    private class Calculate extends RecursiveAction {
        private final int from;
        private final int to;
        private final int add;
        private final int v;

        private Calculate(int from, int to, int add, int v) {
            this.from = from;
            this.to = to;
            this.add = add;
            this.v = v;
        }

        @Override
        protected void compute() {
            if (to - from <= block) {
                int sum = add;
                for (int i = from; i < to; i++) {
                    int nextSum = array[i] + sum;
                    array[i] = sum;
                    sum = nextSum;
                }
                return;
            }
            int middle = (from + to) / 2;
            int left = 2 * v + 1;
            int right = 2 * v + 2;
            invokeAll(
                    new Calculate(from, middle, add, left),
                    new Calculate(middle, to, add + tree[left], right)
            );
        }
    }
}
