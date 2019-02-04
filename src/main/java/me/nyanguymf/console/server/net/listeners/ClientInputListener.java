/**
 * ClientInputListener.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net.listeners;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;
import me.nyanguymf.console.server.net.Connection;
import me.nyanguymf.console.server.net.handlers.AuthHandler;
import me.nyanguymf.console.server.net.handlers.CommandHandler;
import me.nyanguymf.console.server.net.handlers.PacketHandler;
import me.nyanguymf.console.server.types.RequestManager;

/**
 * @author nyanguymf
 */
public final class ClientInputListener implements Observer {
    private RequestManager out;
    private PacketHandler auth;
    private PacketHandler cmd;

    public ClientInputListener(Connection currentConn
                                , JavaPlugin plugin) {

        this.out  = currentConn.getRequestManager();
        this.auth = new AuthHandler(currentConn);
        this.cmd  = new CommandHandler(currentConn, plugin);
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet inputPacket = (Packet) arg;

        switch (inputPacket.getType()) {
        case AUTH_PACKET:
            auth.handle(inputPacket);
            break;
        case INFO:
            Bukkit.getConsoleSender().sendMessage(inputPacket.toString());
            break;
        case COMMAND_PACKET:
            cmd.handle(inputPacket);
            break;
        case INVALID_PACKET_ERROR:
            System.err.printf("Client got invalid packet: %s\n"
                                , inputPacket.getMisc().toString());
            break;

        default:
            System.err.printf("Got unexpected Packet type: %s. Is it up to date?"
                                , inputPacket.toString());
            Packet error = new Packet(PacketType.INVALID_PACKET_ERROR
                                        , "Got unexpected packet type."
                                        , "server");
            try {
                out.sendRequest(error);
            } catch (IOException e) {
                e.printStackTrace();
            }

            break;
        }
    }
}
