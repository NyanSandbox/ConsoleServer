/**
 * AuthHandler.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net.handlers;

import java.io.IOException;
import java.util.Map;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;
import me.nyanguymf.console.server.net.Connection;
import me.nyanguymf.console.server.types.Authorizable;
import me.nyanguymf.console.server.types.Client;
import me.nyanguymf.console.server.types.ClientsConfig;
import me.nyanguymf.console.server.types.ConnectionStorage;

/**
 * @author nyanguymf
 */
public final class AuthHandler extends PacketHandler {
    private ClientsConfig config;
    private ConnectionStorage storage;
    private Connection conn;

    public AuthHandler(Connection currentConn, ClientsConfig config) {
        super(currentConn.getRequestManager());

        this.conn = currentConn;
        this.config = config;
        this.storage = currentConn.getConnStorage();
    }

    @Override
    public void handle(Packet packet) {
        String login    = packet.getSender();
        String password = packet.getBody();

        Map<String, Client> clientsByName = config.getClients();

        if (!clientsByName.containsKey(login)) {
            authError("[AuthHandler] There are no such user for login: " + login + ".");
            return;
        }

        Authorizable client = clientsByName.get(login);

        if (client.isAuthorized()) {
            alreadyAuthorized();
            return;
        }

        if (client.getAuthTries() >= config.getMaxAuthTries()) {
            authError("[AuthHandler] You reach max auth times limit.");
            return;
        }

        if (!client.authorise(password)) {
            authError("[AuthHandler] Wrong password.");
            return;
        }

        this.storage.addAuthorized(this.conn,(Client) client);
        authSuccess();
    }

    /*-----------------------------*
     *       Code duplicates       *
     *-----------------------------*/

    private void authSuccess() {
        Packet answer = new Packet();
        answer.setType(PacketType.AUTH_SUCCESS);
        answer.setBody("[AuthHandler] You have successfully logged in.");
        answer.setSender("[Server]");

        sendPacket(answer);
    }

    private void authError(String error) {
        Packet answer = new Packet();
        answer.setType(PacketType.AUTH_ERROR);
        answer.setBody(error);
        answer.setSender("[Server]");

        sendPacket(answer);
    }

    private void alreadyAuthorized() {
        Packet answer = new Packet();
        answer.setType(PacketType.INFO);
        answer.setBody("[AuthHandler] Already authorized.");
        answer.setSender("[Server]");

        sendPacket(answer);
    }

    private void sendPacket(Packet packet) {
        try {
            super.getRequestManager().sendRequest(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
