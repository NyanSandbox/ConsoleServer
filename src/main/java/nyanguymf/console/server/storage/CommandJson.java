/**
 * This file is the part of Console Server plug-in.
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
package nyanguymf.console.server.storage;

import static nyanguymf.console.server.ConsoleServerPlugin.getUserConfig;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/** @author NyanGuyMF - Vasiliy Bely */
public final class CommandJson implements Permissionable {
    private String command;

    private String args;

    private String username;

    private String hashedPassword;

    public static boolean hasPermission(final Permissionable client) {
        return client.getHashedPassword().equals(md5Hex(getUserConfig().getPassword()))
                && client.getUsername().equals(getUserConfig().getUsername());
    }

    /** @return the username */
    @Override public String getUsername() {
        return username;
    }

    /** Sets username */
    public void setUsername(final String username) {
        this.username = username;
    }

    /** @return the hashedPassword */
    @Override public String getHashedPassword() {
        return hashedPassword;
    }

    /** Sets hashedPassword */
    public void setHashedPassword(final String password) {
        hashedPassword = password;
    }

    /** @return the command */
    public String getCommand() {
        return command;
    }

    /** Sets command */
    public void setCommand(final String command) {
        this.command = command;
    }

    /** @return the args */
    public String getArgs() {
        return args;
    }

    /** Sets args */
    public void setArgs(final String args) {
        this.args = args;
    }
}
