/**
 * This file is the part of Console Client program.
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
package nyanguymf.console.common.command;

import java.util.HashMap;
import java.util.Map;

/** @author NyanGuyMF - Vasiliy Bely */
public final class CommandManager {
    private Map<String, ConsoleCommand> commands;

    public CommandManager() {
        commands = new HashMap<>();
    }

    /**
     * Executes command for given name.
     * <p>
     * Returns <tt>false</tt> if command not found.
     *
     * @param   name    Name of issued command.
     * @param   args    Arguments of command.
     * @return <tt>true</tt> if command was executed.
     */
    public boolean executeCommand(String name, final String args[]) {
        boolean isExecuted = false;

        name = name.toLowerCase();

        if ((commands.size() == 0) || (name == null))
            return isExecuted;

        for (String cmd : commands.keySet()) {
            if (name.equals(cmd)) {
                commands.get(cmd.toLowerCase()).execute(name, args);
                isExecuted = true;
                break; // command executed, now there are no reason to continue.
            }
        }

        if (isExecuted)
            return isExecuted;

        for (ConsoleCommand cmd : commands.values()) {
            for (String alias : cmd.getAliases()) {
                if (alias.equals(name)) {
                    cmd.execute(alias, args);
                    isExecuted = true;
                    break; // command executed, now there are no reason to continue.
                }
            }
        }

        return isExecuted;
    }

    /**
     * Registers new client command.
     *
     * @param   cmd     Command to register for client.
     * @return Old command with the same name.
     */
    public ConsoleCommand registerCommand(final ConsoleCommand cmd) {
        return commands.put(cmd.getName().toLowerCase(), cmd);
    }
}
