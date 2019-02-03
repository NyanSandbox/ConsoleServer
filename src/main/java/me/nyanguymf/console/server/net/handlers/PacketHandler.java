/**
 * PacketHandler.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.net.handlers;

import me.nyanguymf.console.server.types.RequestManager;

/**
 * @author nyanguymf
 */
public abstract class PacketHandler implements Handler {
    private RequestManager out;

    public PacketHandler(RequestManager out) {
        this.out = out;
    }

    protected RequestManager getRequestManager() {
        return this.out;
    }
}
