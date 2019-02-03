/**
 * PacketType.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.2
 */
package me.nyanguymf.console.net;

import java.io.Serializable;

/**
 * @author nyanguymf
 */
public enum PacketType implements Serializable {
    AUTH_PACKET,
    AUTH_SUCCESS,
    AUTH_NEEDED,
    AUTH_ERROR,
    COMMAND_PACKET,
    INVALID_PACKET_ERROR,
    INFO;
}
