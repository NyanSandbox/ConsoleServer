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
package nyanguymf.console.common.event;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides basic implementation on {@link Event}.
 *
 * @author NyanGuyMF - Vasiliy Bely
 */
public abstract class AbstractEvent<Impl extends AbstractEvent<?>> implements Event<Impl> {
    private DefaultHander<Impl> defaultHander;
    private Set<EventListener<Impl>> listeners;

    /**
     * You <b>must</b> run {@link #setImpl(AbstractEvent)}
     * in your implementation constructor. Example:
     * <p>
     * <pre>
     * public Implementation() {
     *     super( event -> {} );
     *     super.setImpl( this );
     * }
     * </pre>
     *
     * @param defaultHander
     */
    public AbstractEvent() {
        listeners = new HashSet<>();
    }

    @Override public final void register(final EventListener<Impl> listener) {
        listeners.add(listener);
    }

    /** @return the defaultHander */
    protected DefaultHander<Impl> getDefaultHander() {
        return defaultHander;
    }

    /** Sets defaultHander */
    protected void setDefaultHander(final DefaultHander<Impl> defaultHander) {
        this.defaultHander = defaultHander;
    }
}
