/**
 * ConnectionStorage.java
 *
 * Copyright 2019.02.03 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.types;

import java.util.Map;

import me.nyanguymf.console.server.net.Connection;
import me.nyanguymf.console.server.net.handlers.AuthHandler;

/**
 * Storage with only authorized connections. Don't use it
 * in order to get all connections.
 *
 * @author  NyanGuyMF
 * @see     AuthHandler
 */
public interface ConnectionStorage {
    /** Gets all authorized connections. */
    public Map<Connection, Authorizable> getAuthorized();

    /** Adds authorized connection. */
    public void addAuthorized(Connection conn, Authorizable client);
}
