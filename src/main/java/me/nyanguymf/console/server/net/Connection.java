/**
 * Connection.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net;

import java.io.IOException;
import java.net.Socket;

import org.bukkit.plugin.java.JavaPlugin;

import me.nyanguymf.console.server.net.listeners.ClientInputListener;
import me.nyanguymf.console.server.types.ClientsConfig;
import me.nyanguymf.console.server.types.ConnectionStorage;
import me.nyanguymf.console.server.types.LocaleStorage;
import me.nyanguymf.console.server.types.RequestManager;

/**
 * @author nyanguymf
 */
public final class Connection {
    private Socket connection;
    private OutputManager out;
    private InputManager in;
    private ConnectionStorage storage;
    private ClientsConfig clientsConfig;
    private LocaleStorage locale;

    /**
     * Creates new I/O system from {@link Socket} connection.
     *
     * @param   socket      Socket connection.
     * @param   config      Configuration with clients to handle authorization.
     * @param   plugin      Main plug-in class instance.
     * @param   locale      Locale manager with plug-in messages.
     *
     * @throws  IOException If unable to create I/O connection.
     */
    public Connection(final Socket socket
                    , ClientsConfig config
                    , JavaPlugin plugin
                    , LocaleStorage locale
                    , ConnectionStorage storage) throws IOException {

        this.locale        = locale;
        this.storage       = storage;
        this.connection    = socket;
        this.clientsConfig = config;

        this.out = new OutputManager(connection.getOutputStream());
        this.in  = new InputManager(connection.getInputStream(), out, plugin, this);

        this.in.runTaskAsynchronously(plugin);

        this.in.registerListener(new ClientInputListener(this, plugin));
        this.out.askPassword();
    }

    /** Closes socket and I/O connections. */
    public void close() {
        try {
            this.connection.close();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Deauthorize current client on close. */
        try {
            this.storage.getAuthorized().get(this).deauthorize();
            this.storage.removeAuthorized(this);
        } catch (NullPointerException expected) {
            // In the case when client doesn't authorized yet.
        }

        this.clientsConfig.save();

        if (!this.in.isCancelled())
            this.in.cancel();
    }

    /** Gets request manager. */
    public RequestManager getRequestManager() {
        return this.out;
    }

    /** Gets locale storage. */
    public LocaleStorage getLocale() {
        return this.locale;
    }

    /** Gets connection storage. */
    public ConnectionStorage getConnStorage() {
        return this.storage;
    }

    public ClientsConfig getClientsConfig() {
        return this.clientsConfig;
    }

    /** Gets client address. */
    public String getAddress() {
        return this.connection.getRemoteSocketAddress().toString();
    }
}
