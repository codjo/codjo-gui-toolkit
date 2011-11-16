/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit;
import net.codjo.gui.toolkit.util.ErrorDialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Composant servant à mapper un champ boolean devant être affiché sous forme de deux
 * checkBox.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.8 $
 */
public class DoubleCheckBoxField extends JPanel {
    public static final String VALUE_PROPERTY_NAME = "check";
    private ButtonGroup buttonGroup1 = new ButtonGroup();
    private JCheckBox non = new JCheckBox();
    private JCheckBox oui = new JCheckBox();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    public DoubleCheckBoxField() {
        try {
            jbInit();
            oui.addChangeListener(new ValueChangeListener());
        }
        catch (Exception ex) {
            ErrorDialog.show(this, "Impossible de créer le composant", ex);
        }
    }

    public void setSelected(boolean selected) {
        oui.setSelected(selected);
        non.setSelected(!selected);
    }


    public boolean isSelected() {
        return oui.isSelected();
    }


    private void jbInit() {
        this.setLayout(gridBagLayout1);
        non.setHorizontalAlignment(SwingConstants.CENTER);
        oui.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(oui,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(non,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        buttonGroup1.add(oui);
        buttonGroup1.add(non);
        this.setPreferredSize(new Dimension(60, 30));
    }

    class ValueChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent ce) {
            firePropertyChange(VALUE_PROPERTY_NAME, !isSelected(), isSelected());
        }
    }
}
