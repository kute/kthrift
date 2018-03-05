package com.kute.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * created by kute on 2018/03/04 12:11
 */
public abstract class AbstractKthriftServer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKthriftServer.class);

    protected TServer server;
    protected TProcessor tprocessor;

    protected Class<?> serviceInterface;
    protected Object serviceImpl;

    protected String hostName;
    protected int port;

    protected long waitTimeBeforeClose;

    protected volatile Thread _hook;

    protected Map<String, TProcessor> serviceProcessorMap = new HashMap<String, TProcessor>();

    public void addShutDownHook() {
        _hook = new Thread(() -> this.close());
        Runtime.getRuntime().addShutdownHook(_hook);
    }

    public void removeShutDownHook() {
        if (_hook != null) {
            Runtime.getRuntime().removeShutdownHook(_hook);
        }
    }

    public abstract void start(boolean daemon) throws Exception;


    public void close() {
        logger.info("Closing kthrift server[{}:{}]", this.hostName, this.port);

        if(this.waitTimeBeforeClose > 0L) {
            try {
                TimeUnit.MILLISECONDS.sleep(waitTimeBeforeClose);
            } catch (InterruptedException e) {
                logger.error("Error occur when waiting {} ms before Closing kthrift server. {}", this.waitTimeBeforeClose, e);
            }
        }
        server.stop();
        removeShutDownHook();
        logger.info("Finished closing kthrift server[{}:{}]", this.hostName, this.port);
    }

    public TServer getServer() {
        return server;
    }

    public void setServer(TServer server) {
        this.server = server;
    }

    public TProcessor getTprocessor() {
        return tprocessor;
    }

    public void setTprocessor(TProcessor tprocessor) {
        this.tprocessor = tprocessor;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Object getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getWaitTimeBeforeClose() {
        return waitTimeBeforeClose;
    }

    public void setWaitTimeBeforeClose(long waitTimeBeforeClose) {
        this.waitTimeBeforeClose = waitTimeBeforeClose;
    }

    public void registProcessor(TProcessor processor) {
        this.tprocessor = processor;
    }
}
