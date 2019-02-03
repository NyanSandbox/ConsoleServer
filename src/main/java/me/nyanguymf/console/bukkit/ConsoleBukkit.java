/**
 * ConsoleBukkit.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.bukkit;

import java.io.File;
import java.nio.file.Path;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration.BukkitYamlProperties;
import me.nyanguymf.console.bukkit.data.ConfigManager;
import me.nyanguymf.console.server.net.ConnectionManager;
import me.nyanguymf.console.server.types.ClientsConfig;
import me.nyanguymf.console.server.types.LocaleStorage;

/**
 * @author nyanguymf
 */
public class ConsoleBukkit extends JavaPlugin {
    private ConnectionManager connectionManager;
    private LocaleStorage locale;
    private BukkitYamlConfiguration clientsConfig;

    @Override
    public void onLoad() {
        locale = new LocaleManager(this);
        locale.init();
    }

    /** Entry point to Bukkit plugin. */
    @Override
    public void onEnable() {
        initDefaultConfig();
        initClientsConfig();
        initConnManager();

        Bukkit.getConsoleSender().sendMessage("§bPlugin enabled.");
    }

    @Override
    public void onDisable() {
        if (connectionManager != null)
            connectionManager.cancel();

        Bukkit.getConsoleSender().sendMessage("§bPlugin disabled.");
    }

    /** Creates default config if doesn't exist. */
    private void initDefaultConfig() {
        File configFile = new File(super.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            super.saveDefaultConfig();
        }

        if (!isCurrentVersion()) {
            /*--------------------------------------------*
             * This code will replace all previous values,*
             * is there another not complicated way?      *
             *--------------------------------------------*/
            super.saveDefaultConfig();
        }
    }

    /** Checks default config version. */
    private boolean isCurrentVersion() {
        String configVersion  = super.getConfig().getString("version");
        String currentVersion = super.getDescription().getVersion();
        return configVersion.equalsIgnoreCase(currentVersion);
    }

    /** Initializes configuration for clients with logins and passwords. */
    private void initClientsConfig() {
        BukkitYamlProperties clientsPropr = BukkitYamlProperties.builder()
                .addFilter(field -> !field.getName().startsWith("ignore"))
                /*---------------------------------------------------*
                 * The same as FieldNameFormatter.LOWER_UNDERSCORE,  *
                 * but with '-' symbol instead of '_'. if like this) *
                 *---------------------------------------------------*/
                .setFormatter(fn -> {
                    StringBuilder builder = new StringBuilder(fn.length());
                    for (char c : fn.toCharArray()) {
                        if (Character.isLowerCase(c)) {
                            builder.append(c);
                        } else if (Character.isUpperCase(c)) {
                            c = Character.toLowerCase(c);
                            builder.append('-').append(c);
                        }
                    }
                    return builder.toString();
                })
                .build();

        Path clientsPath = new File(this.getDataFolder(), "clients.yml").toPath();
        clientsConfig = new ConfigManager(clientsPath, clientsPropr);
        clientsConfig.loadAndSave();
    }

    /** Initializes connection manager. Must be last. */
    private void initConnManager() {
        connectionManager = new ConnectionManager(getConfig().getInt("connection.port")
                                                , (ClientsConfig) clientsConfig
                                                , this
                                                , locale);
        connectionManager.runTaskAsynchronously(this);
    }
}
