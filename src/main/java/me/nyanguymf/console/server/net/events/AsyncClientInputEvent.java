/**
 * ClientInputEvent.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.server.net.events;

import java.util.Observable;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.server.types.RequestManager;

/**
 * @author nyanguymf
 */
public class AsyncClientInputEvent extends Observable implements Runnable {
    private RequestManager out;
    private Packet clientInput;

    public AsyncClientInputEvent(RequestManager out) {
        this.out = out;
    }

    @Override
    public void run() {
        super.notifyObservers(clientInput);
    }

    public void setClientInput(Packet input) {
        this.clientInput = input;
        super.setChanged();
    }

    public RequestManager getRequestManager() {
        return this.out;
    }
}
