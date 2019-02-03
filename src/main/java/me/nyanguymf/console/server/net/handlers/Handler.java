/**
 * Handler.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.net.handlers;

import me.nyanguymf.console.net.Packet;

/**
 * @author nyanguymf
 */
public interface Handler {
    public abstract void handle(Packet packet);
}
