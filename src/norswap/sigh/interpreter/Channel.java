package norswap.sigh.interpreter;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class Channel<T> {
    private ArrayBlockingQueue<T> queue;
    private boolean isOpen;

    public Channel () {
        this.queue = new ArrayBlockingQueue<>(1);
        this.isOpen = true;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public void close(){
        queue.clear();
        queue = null;
        isOpen = false;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel<?> channel = (Channel<?>) o;
        return isOpen == channel.isOpen && Arrays.equals(queue.toArray(), channel.queue.toArray());
    }

    @Override
    public int hashCode () {
        return Objects.hash(queue, isOpen);
    }
}
