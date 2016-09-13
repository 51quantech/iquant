package com.iquant.base;

/**
 * Created by yonggangli on 2016/8/29.
 */
public class Config {

    public String logpath = ".";
    public String ip = "127.0.0.1";
    public int port = 8080;
    public int min_thread = 128;
    public int max_thread = 512;
    //default http config
    public final static boolean useForwardedHeaders = true;
    //The maximum amount of time a connection is allowed to be idle before being closed.
    public final static Duration maxIdleTime = Duration.seconds(60);

    //The number of threads dedicated to accepting connections. If omitted, this defaults to the
    //number of logical CPUs on the current machine.
    public final static int acceptorThreads = Runtime.getRuntime().availableProcessors() / 2;

    // The number of unaccepted requests to keep in the accept queue before refusing connections. If
    // set to -1 or omitted, the system default is used.
    public final static int acceptQueueSize = 12000;
    // The maximum number of buffers to keep in memory.
    public final static int maxBufferCount = 2048;
    // The initial buffer size for reading requests.
    public final static Size requestBufferSize = Size.kilobytes(16);
    public final static Size requestHeaderBufferSize = Size.kilobytes(8);
    public final static Size responseBufferSize = Size.kilobytes(64);
    public final static Size responseHeaderBufferSize = Size.kilobytes(4);
    public final static boolean reuseAddress = true;
    public final static Duration soLingerTime = Duration.seconds(1);
    public final static int lowResourcesConnectionThreshold = 25000;
    public final static Duration lowResourcesMaxIdleTime = Duration.seconds(1);
    public final static Duration shutdownGracePeriod = Duration.seconds(2);
    public final static boolean useServerHeader = false;
    public final static boolean useDirectBuffers = true;
    public final static int acceptorThreadPriorityOffset = 0;
    public final static String DEFAULT_TEMPLATE_FOLDER= "src/main/webapp/WEB-INF/templates/";
    public int getMin_thread() {
        return min_thread;
    }

    public void setMin_thread(int min_thread) {
        this.min_thread = min_thread;
    }

    public int getMax_thread() {
        return max_thread;
    }

    public void setMax_thread(int max_thread) {
        this.max_thread = max_thread;
    }
}