package net.codjo.gui.toolkit.calendar;
import net.codjo.test.common.LogString;
/**
*
*/
class DateSelectionListenerMock implements DateSelectionListener {
    private LogString logString;


    DateSelectionListenerMock(LogString logString) {
        this.logString = logString;
    }


    public void selectionChanged() {
        logString.call("selectionChanged");
    }
}
