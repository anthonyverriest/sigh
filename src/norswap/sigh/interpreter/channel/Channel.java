package norswap.sigh.interpreter.channel;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class Channel<T> {
    private ArrayBlockingQueue<T> queue;
    private boolean isOpen;
    private final int buffer;

    public Channel(int buffer){
        this.buffer = buffer;
        this.queue = new ArrayBlockingQueue<>(buffer);
        this.isOpen = true;
    }

    public void send(Object value) throws BrokenChannel {
        @SuppressWarnings("unchecked")
        T val = (T) value;

        if(isOpen()){
            try{
                this.queue.put(val);
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

    public void close() throws BadChannelDescriptor {
        if(!isOpen())
            throw  new BadChannelDescriptor();
        queue = null;
        isOpen = false;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Channel)) return false;
        Channel<?> channel = (Channel<?>) o;
        return hashCode() == channel.hashCode();
    }

    @Override
    public int hashCode () {
        return (queue == null) ? Objects.hash(isOpen, buffer) : Objects.hash(Arrays.hashCode(queue.toArray()), isOpen, buffer);
    }
}
