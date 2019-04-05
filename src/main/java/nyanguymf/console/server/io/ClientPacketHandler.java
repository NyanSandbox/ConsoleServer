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

import static nyanguymf.console.server.storage.yaml.User.hasPermission;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import nyanguymf.console.common.event.DefaultHander;
import nyanguymf.console.common.net.Packet;
import nyanguymf.console.server.ConsoleServerPlugin;
import nyanguymf.console.server.storage.yaml.User;

/** @author NyanGuyMF - Vasiliy Bely */
final class ClientPacketHandler implements DefaultHander<ClientPacketEvent> {
    @Override public void handle(final ClientPacketEvent event) {
        Packet packet = event.getPacket();

        switch (packet.getType()) {
        case INFO:
            System.out.println(packet.getBody());
            break;
        case STOP:
            // check permissions
            User user;

            try {
                user = new Gson().fromJson(packet.getBody(), User.class);
            } catch (JsonSyntaxException ex) {
                System.err.println("Invalid JSON format: " + ex.getLocalizedMessage());
                break;
            }

            if (hasPermission(user)) {
                ConsoleServerPlugin.getCommandManager().executeCommand("stop", new String[0]);
            }
            break;

        default:
            System.err.println("Unknown packet: " + packet);
            break;
        }
    }
}