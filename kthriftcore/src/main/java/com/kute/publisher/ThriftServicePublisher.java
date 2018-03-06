package com.kute.publisher;

import com.kute.server.AbstractKthriftServer;
import com.kute.server.SimpleKthriftServer;
import com.kute.util.ServerTypeEnum;
import org.apache.thrift.TProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by kute on 2018/03/04 12:02
 */
public class ThriftServicePublisher implements FactoryBean<Object>, ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(ThriftServicePublisher.class);

    /* member */
    private ApplicationContext applicationContext;

    private AbstractKthriftServer server;
    private Class<?> serviceInterface;
    private Object serviceImpl;
    private boolean daemon = true;
    private int port;

    private String serverType = ServerTypeEnum.SIMPLE.getType();

    private static final Map<Integer, Class<?>> port2serviceInterface = new ConcurrentHashMap<Integer, Class<?>>();

    public Object getObject() throws Exception {
        return this;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (port2serviceInterface.containsKey(port)) {
            if (port2serviceInterface.get(port).equals(serviceInterface)) {
                logger.error("Ignore publish " + serviceImpl + ", you have published " + serviceInterface + " on " + port);
            } else {
                throw new IllegalArgumentException("you have published " + port2serviceInterface.get(port) + " on " + port + ", can't republish " + serviceInterface + " on "
                        + port);
            }
            return;
        } else if (serviceInterface != null) {
            port2serviceInterface.put(port, serviceInterface);
        }

        if(!ServerTypeEnum.NETTY.getType().equalsIgnoreCase(serverType)) {
            server = new SimpleKthriftServer();
        }

        // set property
        server.setHostName(getLocalHostName());
        server.setPort(port);
        server.setServiceInterface(serviceInterface);
        server.setServiceImpl(serviceImpl);

        String ifaceName = serviceInterface.getName();
        String serviceName = ifaceName.substring(0, ifaceName.lastIndexOf("$Iface"));

        TProcessor processor = (TProcessor) serviceImpl.getClass().getConstructor(serviceInterface).newInstance(serviceInterface.cast(serviceImpl));

        server.registProcessor(processor);

        server.addShutDownHook();
        server.start(this.daemon);
    }

    private String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("getLocalHostName error", e);
        }
        return "localhost";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        if (null != server) {
            server.close();
        }
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }
}
