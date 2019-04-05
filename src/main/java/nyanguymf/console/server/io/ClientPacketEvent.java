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

import java.util.HashSet;
import java.util.Set;

import nyanguymf.console.common.event.DefaultHander;
import nyanguymf.console.common.event.Event;
import nyanguymf.console.common.event.EventListener;
import nyanguymf.console.common.net.Packet;
import nyanguymf.console.server.net.ClientConnection;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ClientPacketEvent implements Event<ClientPacketEvent> {
    private static Set<EventListener<ClientPacketEvent>> listeners;
    private static DefaultHander<ClientPacketEvent> defaultHander;
    private final ClientConnection conn;
    private final Packet packet;

    public ClientPacketEvent(final ClientConnection conn, final Packet packet) {
        if (ClientPacketEvent.listeners == null) {
            ClientPacketEvent.listeners = new HashSet<>();
        }
        if (ClientPacketEvent.defaultHander == null) {
            ClientPacketEvent.defaultHander = new ClientPacketHandler(conn);
        } else if (!(ClientPacketEvent.defaultHander instanceof ClientPacketHandler)) {
            ClientPacketEvent.defaultHander = new ClientPacketHandler(conn);
        }

        this.conn   = conn;
        this.packet = packet;
    }

    /** @return the conn */
    public ClientConnection getConn() {
        return conn;
    }

    /** @return the packet */
    public Packet getPacket() {
        return packet;
    }

    @Override
    public synchronized void register(final EventListener<ClientPacketEvent> listener) {
        ClientPacketEvent.listeners.add(listener);
    }

    @Override public synchronized void run() {
        ClientPacketEvent.listeners.forEach(listener -> listener.onUpdate(this));
        ClientPacketEvent.defaultHander.handle(this);
    }
}
