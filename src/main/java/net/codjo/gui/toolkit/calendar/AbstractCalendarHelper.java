/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
/**
 * Assistant pour créer une fenêtre Popup avec un calendrier.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public abstract class AbstractCalendarHelper {
    protected JButton okButton;
    protected CallBack callBack;


    protected AbstractCalendarHelper() {
    }


    protected AbstractCalendarHelper(CallBack callBack) {
        this.callBack = callBack;
    }


    public JButton getOkButton() {
        return okButton;
    }


    protected void safeAddCallBackNotifyCancel(JDialog dialog) {
        if (callBack != null) {
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    callBack.notifyCancel();
                    super.windowClosing(event);
                }
            });
        }
    }


    protected void safeCallBackNotifyOk() {
        if (callBack != null) {
            callBack.notifyOk();
        }
    }


    abstract JCalendar newCalendar(final Date selectedDate);


    public void askDialog(final DateFieldInterface dateField, Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);
        final JDialog dialog = newDialog(window);

        // Calendar
        final JCalendar cal = newCalendar(dateField.getDate());
        dialog.getContentPane().add(cal, BorderLayout.CENTER);

        // Ok button
        okButton = new JButton("OK");
        okButton.setName("Calendar.OK");
        JPanel okPanel = new JPanel();
        okPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dateField.setDate(cal.getSelectedDate());

                safeCallBackNotifyOk();
                dialog.dispose();
            }
        });
        safeAddCallBackNotifyCancel(dialog);

        dialog.getContentPane().add(okPanel, BorderLayout.SOUTH);

        // Affiche
        dialog.pack();
        dialog.setLocationRelativeTo(dateField.getCalendarButton());
        correctLocationWithStartMenu(dialog, dateField.getCalendarButton().getLocationOnScreen());
        dialog.setVisible(true);
    }


    public void askDialog(final JTextComponent textField, final JButton calendarButton, String datePattern) {
        TextFieldWrapper formatter = new TextFieldWrapper(textField, calendarButton, datePattern);
        askDialog(formatter, textField);
    }


    protected void correctLocationWithStartMenu(JDialog dialog, Point point) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = dialog.getSize();
        int yAxis = dialog.getLocation().y;

        if (yAxis + dialogSize.height > (screenSize.height - 20)) {
            yAxis -= yAxis + dialogSize.height - (screenSize.height - 20);
        }

        dialog.setLocation(dialog.getLocation().x, yAxis);
    }


    protected JDialog newDialog(Window window) {
        JDialog dialog;
        if (window instanceof Frame) {
            dialog = new JDialog((Frame)window, "Selection de la Date", true);
        }
        else {
            dialog = new JDialog((JDialog)window, "Selection de la Date", true);
        }
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setLayout(new BorderLayout(5, 5));
        return dialog;
    }


    private static class TextFieldWrapper implements DateFieldInterface {
        private final JTextComponent textField;
        private final JButton calendarButton;
        private SimpleDateFormat dateFormatter;


        TextFieldWrapper(JTextComponent textField, JButton calendarButton, String pattern) {
            this.textField = textField;
            this.calendarButton = calendarButton;
            dateFormatter = new SimpleDateFormat(pattern);
        }


        public Date getDate() {
            try {
                return dateFormatter.parse(textField.getText());
            }
            catch (ParseException e) {
                return new Date();
            }
        }


        public void setDate(Date newDate) {
            textField.setText(dateFormatter.format(newDate));
        }


        public JButton getCalendarButton() {
            return calendarButton;
        }
    }
}
