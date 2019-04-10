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

import static nyanguymf.console.server.ConsoleServerPlugin.runTask;
import static nyanguymf.console.server.storage.CommandJson.hasPermission;
import static org.bukkit.Bukkit.dispatchCommand;
import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import nyanguymf.console.common.event.DefaultHander;
import nyanguymf.console.common.net.Packet;
import nyanguymf.console.common.net.PacketType;
import nyanguymf.console.server.net.ClientConnection;
import nyanguymf.console.server.storage.AuthJson;
import nyanguymf.console.server.storage.CommandJson;
import nyanguymf.console.server.storage.Permissionable;

/** @author NyanGuyMF - Vasiliy Bely */
final class ClientPacketHandler implements DefaultHander<ClientPacketEvent> {

    @Override public void handle(final ClientPacketEvent event, final ClientConnection conn) {
        Packet packet = event.getPacket();
        AuthJson auth;

        switch (packet.getType()) {
        case INFO:
            System.out.println(packet.getBody());
            break;
        case REMOTE_COMMAND:
            CommandJson command;

            try {
                command = new Gson().fromJson(packet.getBody(), CommandJson.class);
            } catch (JsonSyntaxException ex) {
                System.err.println("Invalid JSON format: " + ex.getLocalizedMessage());
                break;
            }

            executeCommand(command, conn);
            break;
        case LOG_ENABLE:
            try {
                auth = new Gson().fromJson(packet.getBody(), AuthJson.class);
            } catch (JsonSyntaxException ex) {
                System.err.println("Invalid JSON format: " + ex.getLocalizedMessage());
                break;
            }

            toggleLog(auth, true, conn);
            break;
        case LOG_DISABLE:
            try {
                auth = new Gson().fromJson(packet.getBody(), AuthJson.class);
            } catch (JsonSyntaxException ex) {
                System.err.println("Invalid JSON format: " + ex.getLocalizedMessage());
                break;
            }

            toggleLog(auth, false, conn);
            break;

        default:
            System.err.println("Unknown packet: " + packet);
            break;
        }
    }

    private void toggleLog(
        final AuthJson auth, final boolean logStatus
        , final ClientConnection conn
    ) {
        if (!checkPermission(auth, conn))
            return;

        String status = logStatus
                ? GREEN + "true"
                : RED + "false";

        conn.setLoggable(logStatus);
        getConsoleSender().sendMessage(
            YELLOW + "Client " + conn.getClient().getRemoteSocketAddress()
            + " changed loggable status to " + status
        );
    }

    private void executeCommand(final CommandJson command, final ClientConnection conn) {
        if (!checkPermission(command, conn))
            return;

        runTask(() -> {
            dispatchCommand(
                getConsoleSender(),
                command.getCommand() + " " + command.getArgs()
            );
        });
    }

    private boolean checkPermission(
        final Permissionable permissionable
        , final ClientConnection conn
    ) {
        if (hasPermission(permissionable))
            return true;

        System.out.printf(
            "Client %s use wrong password/username.\n",
            conn.getClient().getRemoteSocketAddress()
        );

        conn.getOutputManager().sendPacket(
                new Packet.PacketBuilder()
                .body("Wrong password or login.")
                .type(PacketType.INFO)
                .build()
        );

        return false;
    }
}
