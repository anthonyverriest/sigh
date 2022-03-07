package norswap.sigh.interpreter;

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
}
