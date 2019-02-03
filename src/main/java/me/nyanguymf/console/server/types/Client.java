/**
 * Client.java
 *
 * Copyright 2019.02.01 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.types;

import org.apache.commons.codec.digest.DigestUtils;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ConfigurationElement;

/**
 * @author nyanguymf
 */
@ConfigurationElement
public final class Client implements Authorizable {
    /** Describes auth status of client. */
    @Comment("Describes auth status of client.")
    private boolean isAuthorized;

    /** Client's login. */
    @Comment("Client's login.")
    private String login;

    /** Hash of client's password. */
    @Comment("Hash of client's password.")
    private String passHash;

    /** Tries of authorization. */
    @Comment("Tries of authorization.")
    private int authTries;

    /** Last client's IP address. */
    @Comment("Last client's IP address.")
    private String lastIp;

    /* Don't use it! Just for ConfigLib. */
    public Client() {
        this("", "", true, "");
    }

    /**
     * Gets Client from {@link Authorizable} instance.
     *
     * @param instance {@link Authorizable} instance to copy.
     */
    public Client(Authorizable instance) {
        this.authTries = instance.getAuthTries();
        this.isAuthorized = instance.isAuthorized();
        this.passHash = instance.getPassHash();
        this.login = instance.getLogin();
    }

    /**
     * Creates new Client with given login and password's hash code.
     *
     * @param login : String value for {@link #login}.
     * @param passHash : String value for {@link #passHash}.
     * @param isHashed : If <tt>false</tt> given password will not hashing.
     */
    public Client(String login, String password, boolean isHashed, String lastIp) {
        this.login = login;
        this.passHash = isHashed ? password : DigestUtils.md5Hex(password);
        this.authTries = 0;
        this.isAuthorized = false;
        this.lastIp = lastIp;
    }

    @Override
    public boolean authorise(String password) {
        ++authTries;

        this.isAuthorized = passHash.equals(DigestUtils.md5Hex(password));

        return isAuthorized();
    }

    @Override
    public boolean isAuthorized() {
        return this.isAuthorized;
    }

    @Override
    public int getAuthTries() {
        return this.authTries;
    }

    @Override
    public String getLogin() {
        return this.login;
    }

    @Override
    public String getPassHash() {
        return this.passHash;
    }

    @Override
    public void deauthorize() {
        this.authTries = 0;
        this.isAuthorized = false;
    }

    @Override
    public String getLastIp() {
        return this.lastIp;
    }

    @Override
    public String updateLastIp(String newIp) {
        String buffer = this.lastIp;
        this.lastIp = newIp;

        return buffer;
    }
}
