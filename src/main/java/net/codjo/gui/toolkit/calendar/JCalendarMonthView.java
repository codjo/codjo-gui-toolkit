package net.codjo.gui.toolkit.calendar;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import net.codjo.gui.toolkit.util.GuiUtil;
/**
 *
 */
public class JCalendarMonthView extends JCalendar {

    private JLabel monthLabel;


    public JCalendarMonthView() {
        monthLabel = new JLabel();
        monthLabel.setFont(GuiUtil.BOLD_FONT);
        monthComboBox.setVisible(false);
        yearComboBox.setVisible(false);

        helpLabel.setVisible(false);

        add(monthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                               GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }


    public void setMonth(int month, String year) {
        calendar.setName("Calendar_" + month);

        monthComboBox.setSelectedIndex(month);
        yearComboBox.setSelectedItem(year);

        monthLabel.setName("MonthLabel_" + month);
        monthLabel.setText(
              capitalizeMonthText((String)monthComboBox.getSelectedItem()) + " " + yearComboBox.getSelectedItem());
    }


    public void enableSelection(boolean enable) {
        calendar.setEnabled(enable);
    }


    private String capitalizeMonthText(String month) {
        return month.substring(0, 1).toUpperCase() + month.substring(1);
    }
}
