/**
 * ConsoleConigurator.java
 *
 * Copyright 2019.02.01 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.types;

import java.util.Map;

/**
 * @author nyanguymf
 */
public interface ClientsConfig {
    /** Gets maximum tries for authorization. */
    public int getMaxAuthTries();

    /**
     * Gets hash code for given user's login.
     * <p>
     * Be careful! This method gives hash for
     * login in lower case. If given login is 
     * "JANE_SmitH" - method will return for
     * login "jane_smith".
     *
     * @param login : User's login in any case.
     * @return User's password hash code.
     */
    public String getPassHash(String login);

    /** Registers new client and returns it. */
    public Authorizable registerNewClient(String login, String password);

    /**
     * Deletes Client for given login.
     *
     * @param login : Client's login to delete.
     * @return Deleted Client or <tt>null</tt> if
     * Client doesn't existed.
     */
    public Authorizable purgeClient(String login);

    /** Gets map with clients. */
    public Map<String, Client> getClients();
}
