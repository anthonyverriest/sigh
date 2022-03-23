package norswap.sigh.interpreter.channel;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class Channel<T> {
    private ArrayBlockingQueue<T> queue;
    private boolean isOpen;

    public Channel(){
        this.queue = new ArrayBlockingQueue<>(1);
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

    public boolean isOpen(){
        return isOpen;
    }

    public void close() throws BadChannelDescriptor {
        if(!isOpen())
            throw  new BadChannelDescriptor();
        queue.clear();
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
        return Objects.hash(Arrays.hashCode(queue.toArray()), isOpen);
    }
}
