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
package nyanguymf.console.server.storage.cache;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nyanguymf.console.server.net.ClientConnection;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ConnectionsCache implements Closeable {
    private volatile Map<String, ClientConnection> cache;

    public ConnectionsCache() {
        cache = new HashMap<>();
    }

    public synchronized Collection<ClientConnection> getConnections() {
        return cache.values();
    }

    public synchronized ClientConnection getCachedConnection(final String address) {
        return cache.get(address);
    }

    public synchronized void addConnection(final ClientConnection conn) {
        cache.put(conn.getClient().getRemoteSocketAddress().toString(), conn);
    }

    public synchronized void removeSocket(final Socket socket) {
        cache.remove(socket.getRemoteSocketAddress().toString());
    }

    public synchronized void removeSocket(final String address) {
        cache.remove(address);
    }

    @Override public void close() throws IOException {
        cache.values().forEach(conn -> {
            try {
                conn.close();
            } catch (IOException expected) {}
            catch (NullPointerException expected) {}
        });
        cache.clear();
    }
}
