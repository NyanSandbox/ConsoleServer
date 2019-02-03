/**
 * Packet.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.1
 */
package me.nyanguymf.console.net;

import java.io.Serializable;

/**
 * @author nyanguymf
 */
public final class Packet implements Serializable {
    private static final long serialVersionUID = -3077054533247975751L;

    private PacketType type;
    
    private String body;

    private String sender;

    private Object misc;

    /**
     * Empty constructor.
     * <p>
     * You <b>must</b> specify all the data through setters.
     * <ul>
     *   <li>{@link #setSender(String)}</li>
     *   <li>{@link #setType(PacketType)}</li>
     *   <li>{@link #setBody(String)}</li>
     *   <li>{@link #setMisc(Object)}</li>
     * </ul>
     */
    public Packet() {
        // Empty...
        // Specify all the parameters by setters.
    }

    /**
     * Creates new packet.
     *
     * @param type : Packet type.
     * @param body : Packet body.
     * @param sender : Packet sender, usually user name.
     */
    public Packet(final PacketType type, final String body, final String sender) {
        this.type = type;
        this.body = body;
        this.sender = sender;

        this.misc = null;
    }

    /**
     * Creates new packet.
     *
     * @param type : Packet type.
     * @param body : Packet body.
     * @param sender : Packet sender, usually user name.
     * @param misc : Another object to send.
     */
    public Packet(final PacketType type, final String body, final String sender, final Object misc) {
        this(type, body, sender);

        this.misc = misc;
    }

    /** Sets packet sender. */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /** Sets packet type. */
    public void setType(PacketType type) {
        this.type = type;
    }

    /** Sets packet body. */
    public void setBody(String body) {
        this.body = body;
    }

    /** Gets miscellaneous object. */
    public void setMisc(Object misc) {
        this.misc= misc ;
    }

    /** Gets sender. */
    public String getSender() {
        return sender;
    }

    /** Gets packet type. */
    public PacketType getType() {
        return type;
    }

    /** Gets packet body. */
    public String getBody() {
        return body;
    }

    /**
     * Gets miscellaneous object.
     * <p>
     * If object doesn't given will return null.
     */
    public Object getMisc() {
        return misc;
    }

    /*------------------------------------------*/
    /**
     * Converts to JSON format.
     * <p>
     * <tt><pre>{
     *  type : ${type},
     *  sender : ${sender},
     *  body : ${body}
     *}</pre></tt>
     */
    @Override
    public String toString() {
        return "{ \"type\" : " + type
                + ", \"sender\" : " + sender
                + ", \"body\" : " + body + "}";
    }
}
