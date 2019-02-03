/**
 * ConfigManager.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.bukkit.data;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import me.nyanguymf.console.server.types.Authorizable;
import me.nyanguymf.console.server.types.Client;
import me.nyanguymf.console.server.types.ClientsConfig;

/**
 * @author nyanguymf
 */
public final class ConfigManager extends BukkitYamlConfiguration implements ClientsConfig {
    @Comment("Maximum tries of authorization for client")
    private int maxAuthTries;

    @Comment("Registered clients")
    @ElementType(Client.class)
    private Map<String, Client> clientsByName;

    public ConfigManager(Path path, BukkitYamlProperties properties) {
        super(path, properties);

        this.maxAuthTries = 4; /* just default value. */
        clientsByName = new TreeMap<>();
        clientsByName.put("NyanGuyMF", new Client("NyanGuyMF", "password", false));
    }

    @Override
    public int getMaxAuthTries() {
        return maxAuthTries;
    }

    @Override
    public String getPassHash(String login) {
        return this.clientsByName.get(login).getPassHash();
    }

    @Override
    public Authorizable registerNewClient(String login, String password) {
        Client client = new Client(login, password, false);

        this.clientsByName.put(client.getLogin(), client);

        return client;
    }

    @Override
    public Authorizable purgeClient(String login) {
        if (this.clientsByName.containsKey(login))
            return null;

        return this.clientsByName.remove(login);
    }

    @Override
    public Map<String, Client> getClients() {
        return clientsByName;
    }
}
