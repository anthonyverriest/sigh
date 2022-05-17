package norswap.sigh.interpreter.channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Routine {
    static final ExecutorService routinesExecutor = Executors.newCachedThreadPool();

    public static void routine(Runnable routine) {
        routinesExecutor.execute(routine);
    }

    public static void shutdown() {
        routinesExecutor.shutdown();
    }
}
