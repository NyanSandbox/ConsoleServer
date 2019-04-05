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
package nyanguymf.console.server;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import nyanguymf.console.common.command.CommandManager;
import nyanguymf.console.server.commands.StopCommand;
import nyanguymf.console.server.net.ConnectionManager;
import nyanguymf.console.server.storage.yaml.NetConfiguration;
import nyanguymf.console.server.storage.yaml.UserConfiguration;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ConsoleServerPlugin extends JavaPlugin {
    private static UserConfiguration userConfig;
    private static NetConfiguration netConfig;
    private static CommandManager commandManager;
    private static ConnectionManager connectionManager;

    /** Load configuration and sockets as soon as it possible. */
    @Override public void onLoad() {
        loadConfiguration();

        ConsoleServerPlugin.commandManager = new CommandManager();
        ConsoleServerPlugin.commandManager.registerCommand(new StopCommand());

        ConsoleServerPlugin.connectionManager = new ConnectionManager().start();
    }

    @Override public void onEnable() {
        super.getLogger().info("Plugin enabled.");
    }

    @Override public void onDisable() {
        try {
            ConsoleServerPlugin.connectionManager.close();
        } catch (IOException ex) {
            System.err.println(
                "Unable to close all connections: "
                + ex.getLocalizedMessage()
            );
        }
    }

    public static UserConfiguration getUserConfig() {
        return ConsoleServerPlugin.userConfig;
    }

    public static CommandManager getCommandManager() {
        return ConsoleServerPlugin.commandManager;
    }

    public static NetConfiguration getNetConfig() {
        return ConsoleServerPlugin.netConfig;
    }

    private void loadConfiguration() {
        if (!super.getDataFolder().exists()) {
            super.getDataFolder().mkdir();
        }

        File netConfigFile  = new File(super.getDataFolder(), "net.yml");
        File userConfigFile = new File(super.getDataFolder(), "user.yml");
        boolean netFileExists = netConfigFile.exists();
        boolean userFileExists = userConfigFile.exists();

        if (!netFileExists) {
            try {
                netConfigFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("Unable to create net file. May cause NPEs");
                return;
            }
        }
        if (!userFileExists) {
            try {
                userConfigFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("Unable to create user file. May cause NPEs");
                return;
            }
        }

        ConsoleServerPlugin.netConfig = new NetConfiguration(super.getDataFolder());
        ConsoleServerPlugin.userConfig = new UserConfiguration(super.getDataFolder());

        if (!netFileExists) {
            ConsoleServerPlugin.netConfig.save();
        }
        if (!userFileExists) {
            ConsoleServerPlugin.userConfig.save();
        }

        ConsoleServerPlugin.userConfig.loadAndSave();
        ConsoleServerPlugin.netConfig.loadAndSave();
    }
}
