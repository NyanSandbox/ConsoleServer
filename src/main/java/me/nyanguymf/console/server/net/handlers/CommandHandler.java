/**
 * CommandHandler.java
 *
 * Copyright 2019.02.04 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net.handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.server.net.Connection;
import me.nyanguymf.console.server.types.Authorizable;
import me.nyanguymf.console.server.types.ClientsConfig;
import me.nyanguymf.console.server.types.ConnectionStorage;
import me.nyanguymf.console.server.types.LocaleStorage;

/**
 * @author nyanguymf
 */
public final class CommandHandler extends PacketHandler {
    private final JavaPlugin plugin;
    private ConnectionStorage storage;
    private ClientsConfig clients;
    private LocaleStorage locale;

    public CommandHandler(Connection conn
                            , final JavaPlugin plugin) {
        super(conn.getRequestManager());

        this.locale  = conn.getLocale();
        this.storage = conn.getConnStorage();
        this.plugin  = plugin;
        this.clients = conn.getClientsConfig();
    }

    @Override
    public void handle(Packet packet) {
        String login = packet.getSender();

        if (!storage.isConnected()) {
            HandlerUtils.notAuthorizedConnection(super.getRequestManager());
            return;
        }

        Authorizable client = clients.getClient(login);

        if (client == null) {
            HandlerUtils.notAuthorizedClient(super.getRequestManager());
            return;
        }

        if (!client.isAuthorized()) {
            HandlerUtils.notAuthorizedClient(super.getRequestManager());
            return;
        }

        String command = packet.getBody();

        new BukkitRunnable() {
            @Override
            public void run() {
                CommandSender sender = Bukkit.getConsoleSender();
                sender.sendMessage(
                    locale.getString("client-command-execute", login, command)
                );

                Bukkit.dispatchCommand(sender, command);
            }
        }.runTask(plugin);
    }
}
