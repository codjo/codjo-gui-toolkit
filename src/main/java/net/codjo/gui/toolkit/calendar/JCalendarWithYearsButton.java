package net.codjo.gui.toolkit.calendar;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
/**
 * Un composant calendrier.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public class JCalendarWithYearsButton extends JCalendar {
    private JButton plusOneYearButton;
    private JButton plusTwoYearButton;
    private JButton plusThreeYearButton;
    private JButton plusFourYearButton;
    private JButton plusFiveYearButton;


    public JCalendarWithYearsButton() {
        setPreferredSize(new Dimension(220, 370));

        plusOneYearButton = new JButton("+1Y");
        plusTwoYearButton = new JButton("+2Y");
        plusThreeYearButton = new JButton("+3Y");
        plusFourYearButton = new JButton("+4Y");
        plusFiveYearButton = new JButton("+5Y");

        plusOneYearButton.setName("plusOneYearButton");
        plusTwoYearButton.setName("plusTwoYearButton");
        plusThreeYearButton.setName("plusThreeYearButton");
        plusFourYearButton.setName("plusFourYearButton");
        plusFiveYearButton.setName("plusFiveYearButton");

        addButtonInJCalendar(plusOneYearButton, 4);
        addButtonInJCalendar(plusTwoYearButton, 5);
        addButtonInJCalendar(plusThreeYearButton, 6);
        addButtonInJCalendar(plusFourYearButton, 7);
        addButtonInJCalendar(plusFiveYearButton, 8);

        plusOneYearButton.addActionListener(new ButtonYearListener(1));
        plusTwoYearButton.addActionListener(new ButtonYearListener(2));
        plusThreeYearButton.addActionListener(new ButtonYearListener(3));
        plusFourYearButton.addActionListener(new ButtonYearListener(4));
        plusFiveYearButton.addActionListener(new ButtonYearListener(5));
    }


    private void addButtonInJCalendar(JButton button, int gridy) {
        add(button,
            new GridBagConstraints(0, gridy, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }


    private class ButtonYearListener implements ActionListener {
        int nbYears;


        private ButtonYearListener(int nbYears) {
            this.nbYears = nbYears;
        }


        public void actionPerformed(ActionEvent event) {
            Date selectedDate = getSelectedDate();
            if (selectedDate != null) {
                yearComboBox.setSelectedIndex(yearComboBox.getSelectedIndex() + nbYears);
                Calendar cal = Calendar.getInstance();
                cal.setTime(selectedDate);
                cal.add(Calendar.YEAR, nbYears);
                setSelectedDate(cal.getTime());
            }
        }
    }
}
