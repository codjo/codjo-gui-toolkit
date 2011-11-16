/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.date;
import java.awt.Insets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.JTextField;
/**
 * Composant graphique pour l'affichage d'un Timestamp. Un evenenement
 * <code>PropertyChangeEvent</code> est lance des que la date est change (par
 * l'utilisateur ou par programme).
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 */
public class TimestampDateField extends JTextField {
    private static final String DATETIME_PROPERTY_NAME = "datetime";
    private Timestamp date = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
    private SimpleDateFormat dayInWeek =
        new SimpleDateFormat("EEEEEEEEEEE dd MMMM yyyy à HH:mm:ss");

    /**
     * Constructeur par défaut.
     */
    public TimestampDateField() {
        setEditable(false);
        setHorizontalAlignment(JTextField.LEFT);
        setMargin(new Insets(0, 3, 0, 0));
    }

    /**
     * Positionne la date.
     *
     * @param newDate La nouvelle date
     */
    public void setDate(Timestamp newDate) {
        Timestamp oldDate = date;
        date = newDate;
        if (newDate != null) {
            super.setText(dateFormat.format(newDate));
            setToolTipText(dayInWeek.format(newDate));
        }
        else {
            super.setText("");
            setToolTipText(null);
        }
        firePropertyChange(DATETIME_PROPERTY_NAME, oldDate, newDate);
    }


    public void setEditable(boolean isEditable) {
        super.setEditable(false);
    }


    public void setText(String text) {}


    /**
     * Retourne la date représenté.
     *
     * @return La date de ce <code>DateField</code>
     */
    public Timestamp getDate() {
        return date;
    }
}
