/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.util.Map;

/**
 *
 */
public interface PreFinalStepAction {
    public void execute(Map data) throws PreFinalStepException;
}
