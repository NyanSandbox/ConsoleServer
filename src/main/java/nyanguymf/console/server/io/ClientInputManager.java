/**
 * This file is the part of Console Server plug-in.
 *
 * Copyright (c) 2019 Vasily
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package nyanguymf.console.server.io;

import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.ChatColor.YELLOW;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import nyanguymf.console.common.net.Packet;
import nyanguymf.console.server.net.ClientConnection;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ClientInputManager extends Thread implements Closeable {
    private ClientConnection conn;
    private InputStream clientInputStream;
    private ObjectInputStream in;

    public ClientInputManager(final ClientConnection conn) {
        super("Input listener of " + conn.getClient().getRemoteSocketAddress());
        this.conn = conn;
        try {
            clientInputStream = conn.getClient().getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override public void run() {
        try {
            in = new ObjectInputStream(clientInputStream);
        } catch (Exception ex) {
            System.err.println(
                "Unable to establish input connection with client: "
                + ex.getLocalizedMessage()
            );
            return;
        }

        Object obj = null;

        while (!currentThread().isInterrupted()) {
            try {
                try {
                    obj = in.readObject();
                } catch (EOFException ex) {
                    getConsoleSender().sendMessage(
                        YELLOW + "Client " + conn.getClient().getRemoteSocketAddress()
                        + " disconnected."
                    );
                    conn.close();
                    break;
                } catch (SocketException e) {
                    getConsoleSender().sendMessage(
                        YELLOW + "Client " + conn.getClient().getRemoteSocketAddress()
                        + " disconnected."
                    );
                    conn.close();
                    break;
                }

                if (!(obj instanceof Packet)) {
                    System.err.println("Got invalid packet from client.");
                    continue;
                }

                Packet packet = (Packet) obj;
                new Thread(new ClientPacketEvent(conn, packet),
                    "Handle server packet thread."
                ).start();

            } catch (ClassNotFoundException | InvalidClassException ex) {
                ex.printStackTrace();
                System.err.println("Got invalid packet from server.");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException expected) {
                // Unable to establish input connection
            }
        }
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ignore) {}
    }
}
