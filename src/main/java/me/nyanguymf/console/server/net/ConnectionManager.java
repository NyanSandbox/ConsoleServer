/**
 * ConnectionManager.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.nyanguymf.console.server.types.Client;
import me.nyanguymf.console.server.types.ClientsConfig;
import me.nyanguymf.console.server.types.ConnectionStorage;
import me.nyanguymf.console.server.types.LocaleStorage;

/**
 * @author nyanguymf
 */
public class ConnectionManager extends BukkitRunnable implements ConnectionStorage {
    private ServerSocket server;
    private int port;
    private List<Connection> connections;
    private Map<Connection, Client> authorized;
    private ClientsConfig config;
    private JavaPlugin plugin;
    private LocaleStorage locale;

    /**
     * @throws IOException : When unable to bind port.
     */
    public ConnectionManager(int port
                            , ClientsConfig config
                            , JavaPlugin plugin
                            , LocaleStorage locale) {
        connections  = new ArrayList<>();
        authorized   = new HashMap<>();
        this.config  = config;
        this.port    = port;
        this.plugin  = plugin;
        this.locale  = locale;
    }

    @Override
    public void run() {
        String       portError = locale.getString("port-bind-error", String.valueOf(port));
        Socket       socket    = null;
        String       connected;

        try {
            server = new ServerSocket(port);
            try {
                while (true) {
                    if (super.isCancelled()) /* break, but not return in,   */
                        break;               /* order to run socket.close() */

                    try {
                        socket = server.accept();
                    } catch (SocketException expected) {
                        break;
                    }

                    connected = locale.getString("new-client-connected"
                                        , socket.getRemoteSocketAddress().toString());
                    Bukkit.getConsoleSender().sendMessage(connected);

                    try {
                        connections.add(new Connection(socket, config, plugin, locale, this));
                    } catch (IOException ex) {
                        socket.close();
                    }
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(portError);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        try {
            if (!this.server.isClosed())
                this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connections.stream().forEach(conn -> conn.close());
    }

    @Override
    public Map<Connection, Client> getAuthorized() {
        return this.authorized;
    }

    @Override
    public void addAuthorized(Connection conn, Client client) {
        this.authorized.put(conn, client);
    }
}
