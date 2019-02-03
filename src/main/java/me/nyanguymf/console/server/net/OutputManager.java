/**
 * OutputManager.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;
import me.nyanguymf.console.server.types.RequestManager;

/**
 * @author nyanguymf
 */
class OutputManager implements RequestManager {
    private ObjectOutputStream out;

    public OutputManager(OutputStream out) throws IOException {
        this.out = new ObjectOutputStream(out);
    }

    public synchronized void sendRequest(Packet request) throws IOException {
        out.writeObject(request);
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }

    public void askPassword() {
        Packet request = new Packet(PacketType.AUTH_NEEDED, "Please, enter password.", "server");
        try {
            this.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
