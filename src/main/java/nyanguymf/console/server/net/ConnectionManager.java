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
package nyanguymf.console.server.net;

import static nyanguymf.console.server.ConsoleServerPlugin.getNetConfig;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import nyanguymf.console.server.storage.cache.ConnectionsCache;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ConnectionManager implements Closeable {
    private static final int TEM_MINUTES_IN_MILLIS = 600_000;
    private ConnectionsCache cache;
    private ServerSocket serverSocket;

    public ConnectionManager() {
        try {
            serverSocket = new ServerSocket(getNetConfig().getPort());
        } catch (IOException ex) {
            System.err.println(
                    "Unable to create server socket: "
                    + ex.getLocalizedMessage()
            );
            return;
        }

        cache = new ConnectionsCache();
    }

    public ConnectionManager start() {
        if (serverSocket == null)
            return this;

        new Thread(() -> {
            while (true) {
                Socket newClient;

                try {
                    newClient = serverSocket.accept();
                } catch (IOException ex) {
                    System.err.println(
                        "Unable to establish new connection: "
                        + ex.getLocalizedMessage()
                    );
                    continue;
                }

                new Thread(() -> {
                    try {
                        newClient.setSoTimeout(ConnectionManager.TEM_MINUTES_IN_MILLIS);
                    } catch (SocketException e) {
                        try {
                            newClient.close();
                        } catch (IOException ignore) {
                            // already closed
                        }
                        System.err.println(
                            "Unable to set timeout for "
                            + newClient.getRemoteSocketAddress()
                        );
                        return;
                    }

                    cache.addConnection(new ClientConnection(newClient));
                }, "Socket " + newClient.getRemoteSocketAddress()).start();
            }
        }, "Socket connection listener").start();

        return this;
    }

    @Override public void close() throws IOException {
        cache.close();
        serverSocket.close();
    }
}
