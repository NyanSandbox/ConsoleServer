/**
 * LocaleStoreage.java
 *
 * Copyright 2019.02.03 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.types;

import java.util.Map;
import java.util.Properties;

/**
 * @author nyanguymf
 */
public interface LocaleStorage {

    /**
     * Gets {@link String} from configuration.
     * <p>
     * Message example: «There are so many players»
     * <p>
     * Method call example: {@code getString("key")}
     *
     * @param   key Key to get message. i.e. structure is like {@link Map}.
     * @return  the value in this property list with the specified key value
     *          or <tt>null</tt> if the key wasn't found.
     *
     * @see     Properties#getProperty(String)
     * @see     #getString(String, String...)
     * @see     #getString(String, boolean, String...)
     */
    public String getString(final String key);

    /**
     * The same as {@link #getString(String)}, but with arguments replaced.
     * <p>
     * Message example: «There are {0} players: {1}.»
     * <p>
     * Method call example: {@code getString("key", "2", "You, me")}
     *
     * @param   key   Key to get message. i.e. structure is like {@link Map}.
     * @param   args  Arguments to replace.
     * @return  message or <tt>null</tt> if the key wasn't found.
     *
     * @see     #getString(String)
     */
    public String getString(final String key, final String... args);

    /**
     * The same as {@link #getString(String)}, but with ability filter colors.
     * <p>
     * Message example: «\u00a76There are so many players.»
     * <p>
     * Method call example: {@code getString("key", false)}
     *
     * @param   key             Key to get message. i.e. structure is like {@link Map}.
     * @param   removeColors    if <tt>true</tt> will return message without colors.
     * @return  message or <tt>null</tt> if the key wasn't found.
     */
    public String getString(final String key, final boolean removeColors);

    /**
     * The same as {@link #getString(String, String...)}, but with ability filter colors.
     * <p>
     * Message example: «\u00a76There are \u00a7c{0} \u00a76players:\u00a7c {1}.»
     * <p>
     * Method call example: {@code getString("key", false, "2", "You, me")}
     *
     * @param   key             Key to get message. i.e. structure is like {@link Map}.
     * @param   removeColors    if <tt>true</tt> will return message without colors.
     * @param   args            
     * @return  message or <tt>null</tt> if the key wasn't found.
     *
     * @see #getString(String)
     * @see #getString(String, String...)
     */
    public String getString(final String key, final boolean removeColors, final String... args);

    /** Unnecessary method to initialize locale store if needed. */
    public default void init() {
        // Nothing to do.
    }
}
