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

import static org.apache.logging.log4j.LogManager.getRootLogger;
import static org.bukkit.Bukkit.getScheduler;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import nyanguymf.console.server.log.ConsoleLogger;
import nyanguymf.console.server.net.ConnectionManager;
import nyanguymf.console.server.storage.cache.ConnectionsCache;
import nyanguymf.console.server.storage.yaml.NetConfiguration;
import nyanguymf.console.server.storage.yaml.UserConfiguration;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ConsoleServerPlugin extends JavaPlugin {
    private static Plugin plugin;
    private static UserConfiguration userConfig;
    private static NetConfiguration netConfig;
    private static ConnectionsCache connectionsCache;
    private ConnectionManager connectionManager;

    public ConsoleServerPlugin() {
        ConsoleServerPlugin.plugin = this;
    }

    /** Load configuration and sockets as soon as it possible. */
    @Override public void onLoad() {
        loadConfiguration();

        connectionManager = new ConnectionManager().start();
        ConsoleServerPlugin.connectionsCache = connectionManager.getCache();
    }

    @Override public void onEnable() {
        ConsoleLogger consoleListener = new ConsoleLogger(connectionManager);
        Logger logger = getRootLogger();
        ((org.apache.logging.log4j.core.Logger)logger).addFilter(consoleListener);

        super.getLogger().info("Plugin enabled.");
    }

    @Override public void onDisable() {
        try {
            connectionManager.close();
        } catch (IOException ex) {
            System.err.println(
                "Unable to close all connections: "
                + ex.getLocalizedMessage()
            );
        }
    }

    public static BukkitTask runTask(final Runnable task) {
        return getScheduler().runTask(ConsoleServerPlugin.plugin, task);
    }

    public static UserConfiguration getUserConfig() {
        return ConsoleServerPlugin.userConfig;
    }

    public static NetConfiguration getNetConfig() {
        return ConsoleServerPlugin.netConfig;
    }

    /** @return the connectionsCache */
    public static ConnectionsCache getConnectionsCache() {
        return ConsoleServerPlugin.connectionsCache;
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
