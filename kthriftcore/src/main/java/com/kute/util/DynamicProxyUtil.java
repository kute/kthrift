package com.kute.util;

import com.kute.server.proxy.SimpleInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DynamicProxyUtil {

    public static <V, R> R createJdkDynamicProxy(Class<R> interfac, V impl) {

        Class[] interfaces = impl.getClass().getInterfaces();

        List<Class> list = new ArrayList<>(interfaces.length);
        for (Class inter : interfaces) {
            list.add(inter);
        }

        if (!list.contains(interfac)) {
            list.add(interfac);
        }

        Class[] newInterfaces = list.toArray(new Class[1]);

        InvocationHandler simpleInvoker = new SimpleInvoker<>(impl);

        return (R) Proxy
                .newProxyInstance(impl.getClass().getClassLoader(), newInterfaces, simpleInvoker);
    }
}
