/**
 * RequestManager.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.types;

import java.io.IOException;

import me.nyanguymf.console.net.Packet;

/**
 * @author nyanguymf
 */
@FunctionalInterface
public interface RequestManager {
    public void sendRequest(Packet request) throws IOException;
}
