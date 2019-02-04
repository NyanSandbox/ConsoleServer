/**
 * HandlerUtils.java
 *
 * Copyright 2019.02.04 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net.handlers;

import java.io.IOException;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;
import me.nyanguymf.console.server.types.RequestManager;

/**
 * @author nyanguymf
 */
final class HandlerUtils {

    public static void notAuthorizedClient(RequestManager out) {
        Packet packet = new Packet();
        packet.setBody("You're not authorized." + '\n');
        packet.setSender("Server");
        packet.setType(PacketType.INFO);

        sendPacket(out, packet);
    }

    public static void notAuthorizedConnection(RequestManager out) {
        Packet packet = new Packet();
        packet.setBody("Your connection isn't authorized. " + '\n');
        packet.setSender("Server");
        packet.setType(PacketType.INFO);

        sendPacket(out, packet);
    }

    private static void sendPacket(RequestManager out, Packet packet) {
        try {
            out.sendRequest(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
