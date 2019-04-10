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
package nyanguymf.console.server.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import nyanguymf.console.common.net.Packet;
import nyanguymf.console.common.net.PacketType;
import nyanguymf.console.server.net.ConnectionManager;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ConsoleLogger implements Filter {
    private ConnectionManager conn;

    public ConsoleLogger(final ConnectionManager conn) {
        this.conn = conn;
    }

    @Override public Result filter(final LogEvent event) {
        String message = event.getMessage().getFormattedMessage();
        Packet packet = new Packet.PacketBuilder()
                .body(message)
                .type(PacketType.LOG_MESSAGE)
                .build();

        conn.getCache().getConnections().forEach(clientConn -> {
            if (clientConn.isLoggable()) {
                clientConn.getOutputManager().sendPacket(packet);
            }
        });

        return null;
    }

    @Override public State getState() {
        return null;
    }

    @Override public void initialize() {

    }

    @Override public void start() {

    }

    @Override public void stop() {

    }

    @Override public boolean isStarted() {
        return false;
    }

    @Override public boolean isStopped() {
        return false;
    }

    @Override public Result getOnMismatch() {
        return null;
    }

    @Override public Result getOnMatch() {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3, final Object p4) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3, final Object p4, final Object p5) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3, final Object p4, final Object p5, final Object p6) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2,
            final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return null;
    }

    @Override public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return null;
    }
}
