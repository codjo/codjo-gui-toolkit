/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
/**
 *
 */
public interface Stringifier {
    public static final Stringifier TO_STRING =
        new Stringifier() {
            public String toString(Object value) {
                return value.toString();
            }
        };

    String toString(Object value);
}
