/**
 * InputThread.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.server.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.Observer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.server.net.events.AsyncClientInputEvent;

/**
 * @author nyanguymf
 */
class InputManager extends BukkitRunnable {
    private AsyncClientInputEvent event;
    private ObjectInputStream in;
    private JavaPlugin plugin;
    private Connection conn;

    /**
     * Creates new input stream.
     * <p>
     * Runs {@link #event} when comes new input object.
     *
     * @param   in          Input stream to create {@link ObjectInputStream}.
     * @param   out         Output manager to send results of handled objects.
     * @param   plugin      Main plug-in class instance.
     * @param   conn        Current connection.
     *
     * @throws  IOException if unable to create Input stream.
     */
    public InputManager(final InputStream in
                    , final OutputManager out
                    , JavaPlugin plugin
                    , Connection conn) throws IOException {

        this.in     = new ObjectInputStream(in);
        this.event  = new AsyncClientInputEvent(out);
        this.plugin = plugin;
        this.conn   = conn;
    }

    @Override
    public void run() {
        Object obj = null;
        Packet packet;

        while (true) {
            if (super.isCancelled())
                break;

            try {
                try {
                    obj = in.readObject();
                } catch (SocketException expected) {
                    System.err.println("Lost connection");
                    this.closeAll();
                    break;
                } catch (EOFException expected) {
                    System.err.println("Lost connection");
                    this.closeAll();
                    break;
                }

                packet = (Packet) obj;

                // run input event handling
                event.setClientInput(packet);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, event);

            } catch (ClassNotFoundException expected) {
                System.err.println("Got invalid object from server. Is it up to date?");
                expected.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /** Registers event listener to {@link #event} */
    public synchronized void registerListener(Observer obs) {
        event.addObserver(obs);
    }

    /** Closes Input connection and stops thread. */
    @Override
    public void cancel() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.cancel();
    }

    private void closeAll() {
        this.conn.close();
    }
}
