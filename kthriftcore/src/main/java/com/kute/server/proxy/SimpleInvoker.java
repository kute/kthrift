package com.kute.server.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * created by kute on 2018/03/05 21:58
 */
public class SimpleInvoker<V> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleInvoker.class);

    private V proxy;

    public SimpleInvoker(V proxy) {
        this.proxy = proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        try {
            return method.invoke(proxy, args);
        } catch (Exception e) {
            Throwable cause = (e instanceof InvocationTargetException) ? e.getCause() : e;
            if (cause == null) {
                cause = e;
            }
            logger.error("SimpleInvoker error", cause);
            throw e;
        }
    }
}
