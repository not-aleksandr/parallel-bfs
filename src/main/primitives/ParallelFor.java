package primitives;


import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class ParallelFor extends RecursiveAction {
    private final int block;
    private final int from;
    private final int to;
    private final Consumer<Integer> action;

    public ParallelFor(int block, int from, int to, Consumer<Integer> action) {
        this.from = from;
        this.to = to;
        this.block = block;
        this.action = action;
    }

    @Override
    public void compute() {
        if (to - from <= block) {
            for (int i = from; i < to; i++) {
                action.accept(i);
            }
        } else {
            int middle = (from + to) / 2;
            var leftTask = new ParallelFor(block, from, middle, action).fork();
            new ParallelFor(block, middle, to, action).compute();
            leftTask.join();
        }
    }
}