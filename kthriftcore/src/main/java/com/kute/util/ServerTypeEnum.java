package com.kute.util;

/**
 * created by kute on 2018/03/04 13:24
 */
public enum ServerTypeEnum {

    SIMPLE("simple"),

    NETTY("netty");

    private String type;

    ServerTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
