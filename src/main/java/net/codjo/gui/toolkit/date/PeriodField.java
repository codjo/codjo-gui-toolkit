package net.codjo.gui.toolkit.date;
import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 */
public class PeriodField extends JTextField {
    private static final int MAX_SIZE = 6;
    public static final String PERIOD_VALID_PROPERTY = "periodValid";
    private boolean limitMaxSize;


    public PeriodField() {
        setColumns(MAX_SIZE);
        setName("PeriodField");
        setToolTipText("Format aaaaMM (e.g. '200405')");
        setDocument(new FixedSizePlainDocument());
        CheckPeriodListener checkPeriodListener = new CheckPeriodListener();
        getDocument().addDocumentListener(checkPeriodListener);
    }


    public PeriodField(boolean limitMaxSize) {
        this();
        this.limitMaxSize = limitMaxSize;
    }


    public void colorPeriod() {
        if (isPeriod(getText())) {
            setForeground(Color.black);
            firePropertyChange(PeriodField.PERIOD_VALID_PROPERTY, false, true);
        }
        else {
            setForeground(Color.red);
            firePropertyChange(PeriodField.PERIOD_VALID_PROPERTY, true, false);
        }
    }


    public boolean isCorrect() {
        return isPeriod(getText());
    }


    public static String getTodayDate() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }


    public static boolean isPeriod(String period) {
        String query = period.trim() + "01";
        Date date;
        SimpleDateFormat periodFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            date = periodFormat.parse(query);
        }
        catch (ParseException e) {
            return false;
        }
        return query.equals(periodFormat.format(date));
    }


    private class CheckPeriodListener implements DocumentListener {

        public void insertUpdate(DocumentEvent event) {
            colorPeriod();
        }


        public void removeUpdate(DocumentEvent event) {
            colorPeriod();
        }


        public void changedUpdate(DocumentEvent event) {
            colorPeriod();
        }
    }

    class FixedSizePlainDocument extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet attributeSet)
              throws BadLocationException {

            try {
                Integer.parseInt(str);
                if (!limitMaxSize || (getLength() + str.length()) <= MAX_SIZE) {
                    super.insertString(offs, str, attributeSet);
                }
            }
            catch (NumberFormatException exception) {
                ;
            }
        }
    }
}
