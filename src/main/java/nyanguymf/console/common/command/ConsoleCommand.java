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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** @author NyanGuyMF - Vasiliy Bely */
public abstract class ConsoleCommand {
    private String name;
    private Set<String> aliases;
    private ConsoleCommandExecutor executor;

    public ConsoleCommand(final String name) {
        this.name = name;
        aliases = new HashSet<>();
    }

    public ConsoleCommand(final String name, final Set<String> aliases) {
        this(name);
        this.aliases = aliases;
    }

    public void execute(final String alias, final String[] args) {
        executor.execute(this, alias, args);
    }

    /** @return the name */
    public String getName() {
        return name;
    }

    /** @return the aliases */
    public Set<String> getAliases() {
        return aliases;
    }

    /** Sets executor */
    public void setExecutor(final ConsoleCommandExecutor executor) {
        this.executor = executor;
    }

    /** Sets name */
    protected void setName(final String name) {
        this.name = name;
    }

    /** Sets aliases */
    protected void setAliases(final Set<String> aliases) {
        this.aliases = aliases;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof ConsoleCommand))
            return false;

        ConsoleCommand other = (ConsoleCommand) obj;
        return Objects.equals(name, other.name);
    }
}
