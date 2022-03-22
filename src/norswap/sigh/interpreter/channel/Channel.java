package norswap.sigh.interpreter.channel;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class Channel<T> {
    private ArrayBlockingQueue<T> queue;
    private boolean isOpen;

    public Channel(){
        this.queue = new ArrayBlockingQueue<>(1);
        this.isOpen = true;
    }

    public void send(Object message) {
        @SuppressWarnings("unchecked")
        T msg = (T) message;

        if(isOpen()){
            try{
                this.queue.put(msg);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }else{
            throw new BrokenChannel();
        }
    }

    public T receive(){
        if(isOpen()){
            try{
                return this.queue.take();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        throw new BrokenChannel();
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
        if (!(o instanceof Channel)) return false;
        Channel<?> channel = (Channel<?>) o;
        return isOpen == channel.isOpen && Objects.equals(queue, channel.queue);
    }

    @Override
    public int hashCode () {
        return Objects.hash(queue, isOpen);
    }
}
