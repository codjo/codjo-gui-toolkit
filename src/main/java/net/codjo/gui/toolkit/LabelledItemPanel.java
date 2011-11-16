/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit;
import net.codjo.gui.toolkit.date.DateField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * This class provides a panel for laying out labelled elements neatly with all the labels and elements
 * aligned down the screen. <a href="http://www.javaworld.com/javaworld/jw-10-2002/jw-1004-dialog.html">
 * article</a>
 *
 * @author David Fraser
 * @author Michael Harris
 * @version $Revision: 1.13 $
 */
public class LabelledItemPanel extends JPanel {
    /**
     * The row to add the next labelled item to.
     */
    private int myNextItemRow = 0;
    private int labelAnchor = GridBagConstraints.NORTHEAST;
    private JLabel verticalFillLabel = new JLabel();


    /**
     * This method is the default constructor.
     */
    public LabelledItemPanel() {
        init(new Insets(10, 0, 0, 0));
    }


    public LabelledItemPanel(Insets insets) {
        init(insets);
    }


    public void setLabelLeftAlignment(boolean value) {
        if (value) {
            labelAnchor = GridBagConstraints.NORTHWEST;
        }
        else {
            labelAnchor = GridBagConstraints.NORTHEAST;
        }
    }


    /**
     * This method adds a labelled item to the panel. The item is added to the row below the last item added.
     *
     * @param labelText The label text for the item.
     * @param item      The item to be added.
     */
    public void addItem(String labelText, JComponent item) {
        addItem(labelText, item, new Insets(10, 10, 0, 0), new Insets(10, 10, 0, 10));
    }


    public void addItem(String labelText, JPanel panel, boolean fillVertical) {
        JLabel label = new JLabel(labelText);
        GridBagConstraints labelConstraints = new GridBagConstraints();

        labelConstraints.gridx = 0;
        labelConstraints.gridy = myNextItemRow;
        labelConstraints.insets = new Insets(10, 10, 0, 0);
        labelConstraints.anchor = labelAnchor;
        labelConstraints.fill = GridBagConstraints.NONE;

        add(label, labelConstraints);

        GridBagConstraints itemConstraints = new GridBagConstraints();

        itemConstraints.gridx = 1;
        itemConstraints.gridy = myNextItemRow;
        itemConstraints.insets = new Insets(10, 10, 0, 10);
        itemConstraints.weightx = 1.0;
        itemConstraints.anchor = GridBagConstraints.WEST;
        if (fillVertical) {
            itemConstraints.weighty = 1.0;
            itemConstraints.fill = GridBagConstraints.BOTH;
            remove(verticalFillLabel);
        }
        else {
            itemConstraints.weighty = 0.0;
            itemConstraints.fill = GridBagConstraints.HORIZONTAL;
        }
        add(panel, itemConstraints);

        myNextItemRow++;
    }


    public void addItem(String labelText, JComponent item, Insets labelInsets, Insets itemInset) {
        JLabel label = new JLabel(labelText);
        if (item != null && item.getName() != null) {
            label.setName(item.getName() + ".label");
        }

        GridBagConstraints labelConstraints = new GridBagConstraints();

        labelConstraints.gridx = 0;
        labelConstraints.gridy = myNextItemRow;
        labelConstraints.insets = labelInsets;
        labelConstraints.anchor = labelAnchor;
        labelConstraints.fill = GridBagConstraints.NONE;

        add(label, labelConstraints);

        GridBagConstraints itemConstraints = new GridBagConstraints();

        itemConstraints.gridx = 1;
        itemConstraints.gridy = myNextItemRow;
        itemConstraints.insets = itemInset;

        itemConstraints.anchor = GridBagConstraints.WEST;

        if (item instanceof DateField) {
            itemConstraints.weightx = 0.0;
            itemConstraints.weighty = 0.0;
            itemConstraints.fill = GridBagConstraints.NONE;
        }
        else if (item instanceof JPanel || item instanceof JScrollPane) {
            itemConstraints.weightx = 1.0;
            itemConstraints.weighty = 1.0;
            itemConstraints.fill = GridBagConstraints.BOTH;
            remove(verticalFillLabel);
        }
        else {
            itemConstraints.weightx = 1.0;
            itemConstraints.weighty = 0.0;
            itemConstraints.fill = GridBagConstraints.HORIZONTAL;
        }
        add(item, itemConstraints);

        myNextItemRow++;
    }


    public void addItem(JComponent item) {
        addItem(item, new Insets(10, 10, 0, 10));
    }


    public void addItem(JComponent item, Insets itemInset) {
        GridBagConstraints itemConstraints = new GridBagConstraints();

        itemConstraints.gridx = 0;
        itemConstraints.gridwidth = 2;
        itemConstraints.gridy = myNextItemRow;
        itemConstraints.insets = itemInset;
        itemConstraints.weightx = 1.0;
        itemConstraints.anchor = GridBagConstraints.WEST;
        itemConstraints.fill = GridBagConstraints.HORIZONTAL;

        add(item, itemConstraints);

        myNextItemRow++;
    }


    /**
     * This method initialises the panel and layout manager.
     */
    private void init(Insets insets) {
        setLayout(new GridBagLayout());

        // Create a blank label to use as a vertical fill so that the
        // label/item pairs are aligned to the top of the panel and are not
        // grouped in the centre if the parent component is taller than
        // the preferred size of the panel.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 99;
        constraints.insets = insets;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.VERTICAL;

        add(verticalFillLabel, constraints);
    }
}
