package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.util.Date;
/**
 *
 */
public interface DateHighlighter {

    boolean highlight(Date date);


    Color getHighlightForeground();


    Color getHighlightBackground();
}
