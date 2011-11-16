/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
/**
 * Implements user interface and generates FindFilter for selecting files by name.
 *
 * @author Thierrou
 * @version $Revision: 1.4 $
 */
class FindByName extends JPanel implements FindFilterFactory {
    protected static final String NAME_CONTAINS = "contient";
    protected static final String NAME_IS = "est";
    protected static final String NAME_STARTS_WITH = "commence par";
    protected static final String NAME_ENDS_WITH = "fini par";
    protected static final int NAME_CONTAINS_INDEX = 0;
    protected static final int NAME_IS_INDEX = 1;
    protected static final int NAME_STARTS_WITH_INDEX = 2;
    protected static final int NAME_ENDS_WITH_INDEX = 3;
    private String[] criteria =
        {NAME_CONTAINS, NAME_IS, NAME_STARTS_WITH, NAME_ENDS_WITH};
    private JTextField nameField = null;
    private JComboBox combo = null;
    private JCheckBox ignoreCaseCheck = null;

    FindByName() {
        setLayout(new BorderLayout());

        // Grid Layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 2, 2));

        // Name
        combo = new JComboBox(criteria);
        combo.setFont(new Font("Helvetica", Font.PLAIN, 10));
        combo.setPreferredSize(combo.getPreferredSize());
        panel.add(combo);

        nameField = new JTextField(12);
        nameField.setFont(new Font("Helvetica", Font.PLAIN, 10));
        panel.add(nameField);

        // ignore case
        panel.add(new JLabel("", SwingConstants.RIGHT));

        ignoreCaseCheck = new JCheckBox("ignorer la case", true);
        ignoreCaseCheck.setForeground(Color.black);
        ignoreCaseCheck.setFont(new Font("Helvetica", Font.PLAIN, 10));
        panel.add(ignoreCaseCheck);

        add(panel, BorderLayout.NORTH);
    }

    public String[] getCriteria() {
        return criteria;
    }


    public JTextField getNameField() {
        return nameField;
    }


    public JComboBox getCombo() {
        return combo;
    }


    public JCheckBox getIgnoreCaseCheck() {
        return ignoreCaseCheck;
    }


    public FindFilter createFindFilter() {
        return new NameFilter(nameField.getText(), combo.getSelectedIndex(),
            ignoreCaseCheck.isSelected());
    }

    /**
     * Filter object for selecting files by name.
     *
     * @author Thierrou
     * @version $Revision: 1.4 $
     */
    class NameFilter implements FindFilter {
        private String match = null;
        private int howToMatch = -1;
        private boolean ignoreCase = true;

        NameFilter(String name, int how, boolean ignore) {
            match = name;
            howToMatch = how;
            ignoreCase = ignore;
        }

        public String getMatch() {
            return match;
        }


        public int getHowToMatch() {
            return howToMatch;
        }


        public boolean isIgnoreCase() {
            return ignoreCase;
        }


        public boolean accept(File file, FindProgressCallback callback) {
            if (file == null) {
                return false;
            }

            if ((match == null) || (match.length() == 0)) {
                return true;
            }
            if (howToMatch < 0) {
                return true;
            }

            String filename = file.getName();

            if (howToMatch == NAME_CONTAINS_INDEX) {
                if (ignoreCase) {
                    return filename.toLowerCase().indexOf(match.toLowerCase()) >= 0;
                }
                else {
                    return filename.indexOf(match) >= 0;
                }
            }
            else if (howToMatch == NAME_IS_INDEX) {
                if (ignoreCase) {
                    return filename.equalsIgnoreCase(match);
                }
                else {
                    return filename.equals(match);
                }
            }
            else if (howToMatch == NAME_STARTS_WITH_INDEX) {
                if (ignoreCase) {
                    return filename.toLowerCase().startsWith(match.toLowerCase());
                }
                else {
                    return filename.startsWith(match);
                }
            }
            else if (howToMatch == NAME_ENDS_WITH_INDEX) {
                if (ignoreCase) {
                    return filename.toLowerCase().endsWith(match.toLowerCase());
                }
                else {
                    return filename.endsWith(match);
                }
            }

            return true;
        }
    }
}
