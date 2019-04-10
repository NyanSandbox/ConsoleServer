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

import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.ChatColor.YELLOW;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import nyanguymf.console.server.io.ClientInputManager;
import nyanguymf.console.server.io.ClientOutputManager;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ClientConnection implements Closeable {
    private ClientInputManager inputManager;
    private ClientOutputManager outputManager;
    private Socket client;
    private boolean isLoggable = false;

    public ClientConnection(final Socket socket) {
        client = socket;
        try {
            outputManager = new ClientOutputManager(client.getOutputStream());
            inputManager = new ClientInputManager(this);

            inputManager.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** @return the client */
    public Socket getClient() {
        return client;
    }

    /** Sets client */
    protected void setClient(final Socket client) {
        this.client = client;
    }

    /** @return the inputManager */
    public ClientInputManager getInputManager() {
        return inputManager;
    }

    /** Sets inputManager */
    protected void setInputManager(final ClientInputManager inputManager) {
        this.inputManager = inputManager;
    }

    /** @return the outputManager */
    public ClientOutputManager getOutputManager() {
        return outputManager;
    }

    /** @return the isLoggable */
    public boolean isLoggable() {
        return isLoggable;
    }

    /** Sets isLoggable */
    public void setLoggable(final boolean isLoggable) {
        this.isLoggable = isLoggable;
    }

    @Override public void close() throws IOException {
        getConsoleSender().sendMessage(
            YELLOW + "Closed connection with " + client.getRemoteSocketAddress()
        );

        inputManager.close();
        client.close();
    }

    @Override public int hashCode() {
        return Objects.hash(client.getRemoteSocketAddress().toString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof ClientConnection))
            return false;

        ClientConnection other = (ClientConnection) obj;
        return Objects.equals(
            client.getRemoteSocketAddress().toString(),
            other.client.getRemoteSocketAddress().toString()
        );
    }
}
