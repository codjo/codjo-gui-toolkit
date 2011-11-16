/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
/**
 * <code>JFileChooser</code> accessory for management of shortcuts to  frequently
 * accessed directories and files. <a
 * href="http://www.javaworld.com/javaworld/jw-08-2002/jw-0830-jfile_p.html">article
 * javaworld</a>
 *
 * @author Slav Boleslawski
 */
public class ShortcutsAccessory extends JPanel {
    private static final int TOOLTIP_DISMISS_DELAY = 2000;
    private static final int TOOLTIP_INITIAL_DELAY = 300;
    private static final String APPLICATION_NAME = "JAVA";
    private static final Logger APP = Logger.getLogger(ShortcutsAccessory.class);
    private JFileChooser chooser;
    private String initialTitle = null;
    private int originalInitialDelay;
    private int originalDismissDelay;
    private JButton addButton;
    private JButton deleteButton;
    private JButton aliasButton;
    private JList list;
    private JScrollPane listScrollPane;
    private JTextField aliasField;
    private DefaultListModel model;
    private boolean shortcutsChanged;

    public ShortcutsAccessory(JFileChooser chooser) {
        setName(" Favoris ");
        this.chooser = chooser;
        updateTitle();
        setGUI();
        addListeners();
    }

    /**
     * Creates GUI for this accessory.
     */
    private void setGUI() {
        setLayout(new BorderLayout());

        model = createModel();
        list =
            new JList(model) {
                    @Override
                    public String getToolTipText(MouseEvent me) {
                        if (model.size() == 0) {
                            return null;
                        }

                        Point point = me.getPoint();
                        Rectangle bounds =
                            list.getCellBounds(model.size() - 1, model.size() - 1);
                        int lastElementBaseline = bounds.y + bounds.height;

                        //Is the mouse pointer below the last element in the list?
                        if (lastElementBaseline < point.y) {
                            return null;
                        }

                        int index = list.locationToIndex(point);
                        if (index == -1) { // for compatibility with Java 1.3 and earlier versions
                            return null;
                        }

                        Shortcut shortcut = (Shortcut)model.get(index);
                        String path = shortcut.getPath();
                        if (shortcut.hasAlias()) {
                            return path;
                        }

                        FontMetrics fm = list.getFontMetrics(list.getFont());
                        int textWidth = SwingUtilities.computeStringWidth(fm, path);
                        if (textWidth <= listScrollPane.getSize().width) {
                            return null;
                        }

                        return path;
                    }
                };
        list.setCellRenderer(new ListCellRenderer() {
                public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                    Shortcut shortcut = (Shortcut)value;
                    String name = shortcut.getDisplayName();
                    JLabel label = new JLabel(name);
                    label.setBorder(new EmptyBorder(0, 3, 0, 3));
                    label.setOpaque(true);
                    if (!isSelected) {
                        label.setBackground(list.getBackground());
                        label.setForeground(shortcut.getColor());
                    }
                    else {
                        label.setBackground(list.getSelectionBackground());
                        label.setForeground(list.getSelectionForeground());
                    }
                    return label;
                }
            });
        listScrollPane = new JScrollPane(list);

        buildGui();
    }


    private void buildGui() {
        originalInitialDelay = ToolTipManager.sharedInstance().getInitialDelay();
        originalDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
        ToolTipManager.sharedInstance().setDismissDelay(TOOLTIP_DISMISS_DELAY);
        ToolTipManager.sharedInstance().setInitialDelay(TOOLTIP_INITIAL_DELAY);
        ToolTipManager.sharedInstance().registerComponent(list);

        add(listScrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        addButton = new JButton(new ImageIcon(this.getClass().getResource("add.png")));
        addButton.setBorder(null);
        addButton.setToolTipText("Ajoute le répertoire/fichier courant aux favoris");
        deleteButton =
            new JButton(new ImageIcon(this.getClass().getResource("remove.png")));
        deleteButton.setBorder(null);
        deleteButton.setToolTipText("Efface le favori");
        aliasButton = new JButton(new ImageIcon(this.getClass().getResource("ok.png")));
        aliasButton.setBorder(null);
        aliasButton.setToolTipText("Positionne un alias pour le favori");
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(addButton);
        southPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        southPanel.add(deleteButton);
        southPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        southPanel.add(new JLabel("  Alias:"));
        southPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        aliasField = new JTextField(12);
        aliasField.setMaximumSize(aliasField.getPreferredSize());
        southPanel.add(aliasField);
        southPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        southPanel.add(aliasButton);
        southPanel.add(Box.createHorizontalGlue());
        add(southPanel, BorderLayout.SOUTH);

        int southPanelWidth = southPanel.getPreferredSize().width;
        Dimension size = new Dimension(southPanelWidth, 0);

        //Makes sure the accessory is not resized with addition of entries
        //longer than the current accessory width.
        setPreferredSize(size);
        setMaximumSize(size);
    }


    /**
     * Adds all listeners required by this accessory.
     */
    private void addListeners() {
        //Updates chooser's title
        chooser.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent event) {
                    String propertyName = event.getPropertyName();
                    if (propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                        updateTitle();
                    }
                }
            });

        //Saves shortcuts when the chooser is disposed
        chooser.addAncestorListener(new AncestorListener() {
                public void ancestorRemoved(AncestorEvent event) {
                    ToolTipManager.sharedInstance().setDismissDelay(originalDismissDelay);
                    ToolTipManager.sharedInstance().setInitialDelay(originalInitialDelay);
                    if (shortcutsChanged) {
                        saveShortcuts();
                    }
                }


                public void ancestorAdded(AncestorEvent event) {}


                public void ancestorMoved(AncestorEvent event) {}
            });

        //Sets chooser's current directory or file and updates the Alias field
        list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex == -1) {
                        return;
                    }

                    Shortcut shortcut = (Shortcut)model.get(selectedIndex);
                    String alias = shortcut.getAlias();
                    String path = shortcut.getPath();
                    String color = shortcut.getColorString();

                    String aliasText = alias;
                    if (!"black".equals(color)) {
                        aliasText = color + '#' + alias;
                    }
                    aliasField.setText(aliasText);
                    File file = new File(path);
                    if (file.isFile()) {
                        chooser.setSelectedFile(file);
                    }
                    else {
                        chooser.setCurrentDirectory(file);
                        chooser.setSelectedFile(null);
                    }
                }
            });

        //Adds/deletes/edits a shortcut
        ActionListener actionListener =
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (deleteButton == ae.getSource()) {
                        int ind = list.getSelectedIndex();
                        if (ind == -1) {
                            return;
                        }

                        aliasField.setText("");
                        model.remove(ind);
                    }
                    if (addButton == ae.getSource()) {
                        String path;
                        File file = chooser.getSelectedFile();
                        if (file != null) {
                            path = file.getAbsolutePath();
                        }
                        else {
                            File dir = chooser.getCurrentDirectory();
                            path = dir.getAbsolutePath();
                        }
                        insertShortcut(new Shortcut("", path, "black"));
                    }
                    if (aliasButton == ae.getSource()) {
                        setAlias();
                    }
                    list.clearSelection();
                    chooser.setSelectedFile(null);
                    shortcutsChanged = true;
                }
            };
        addButton.addActionListener(actionListener);
        deleteButton.addActionListener(actionListener);
        aliasButton.addActionListener(actionListener);
        aliasField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent ke) {
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        setAlias();
                        shortcutsChanged = true;
                    }
                }
            });
    }


    /**
     * Creates/edits/deletes an alias for a shortcut.
     */
    private void setAlias() {
        int ind = list.getSelectedIndex();
        if (ind == -1) {
            list.requestFocus();
            return;
        }

        Shortcut shortcut = (Shortcut)model.get(ind);
        String text = aliasField.getText().trim();
        if (text.length() == 0) { //alias removed
            shortcut.setAlias("");
            shortcut.setColor("black");
            model.remove(ind);
            insertShortcut(new Shortcut("", shortcut.getPath(), ""));
            return;
        }

        String color = "black";
        String alias = text;
        int hashIndex = text.indexOf("#");
        if (hashIndex != -1) {
            alias = text.substring(hashIndex + 1);
            color = text.substring(0, hashIndex);
        }
        shortcut.setAlias(alias);
        shortcut.setColor(color);
        aliasField.setText("");
        model.remove(ind);
        insertShortcut(new Shortcut(alias, shortcut.getPath(), color));
    }


    /**
     * Inserts a new shortcut into the list so that list's alphabetical order  is
     * preserved.
     *
     * @param newShortcut
     */
    private void insertShortcut(Shortcut newShortcut) {
        if (model.getSize() == 0) {
            model.addElement(newShortcut);
            return;
        }

        //Checks if newShortcut already exists
        for (int i = 0; i < model.getSize(); i++) {
            Shortcut shortcut = (Shortcut)model.get(i);
            if (shortcut.getPath().equalsIgnoreCase(newShortcut.getPath())) {
                return;
            }
        }

        int insertIndex = 0;
        String newName = newShortcut.getName();
        for (int i = 0; i < model.getSize(); i++) {
            Shortcut shortcut = (Shortcut)model.get(i);
            String name = shortcut.getName();
            if (name.compareToIgnoreCase(newName) <= 0) {
                insertIndex = i + 1;
            }
            else {
                break;
            }
        }
        model.insertElementAt(newShortcut, insertIndex);
    }


    /**
     * Creates a {@link DefaultListModel} and populates it with shortcuts read from a
     * file  in user's home directory.
     *
     * @return the {@link DefaultListModel}
     */
    private DefaultListModel createModel() {
        DefaultListModel listModel = new DefaultListModel();
        String filePath =
            System.getProperty("user.home") + System.getProperty("file.separator")
            + APPLICATION_NAME + ".accessory.dirs";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return listModel;
            }

            BufferedReader in = new BufferedReader(new FileReader(file));
            String buf;
            while ((buf = in.readLine()) != null) {
                if (buf.startsWith("//")) { // Ignores lines with comments
                    continue;
                }
                int commaIndex = buf.indexOf(",");
                if (commaIndex == -1) {
                    throw new IOException("Incorrect format of a " + file.getPath()
                        + " file");
                }
                String colorAndAlias = buf.substring(0, commaIndex).trim();
                String alias;
                String color;
                int hashIndex = colorAndAlias.indexOf("#");
                if (hashIndex != -1) {
                    alias = colorAndAlias.substring(hashIndex + 1);
                    color = colorAndAlias.substring(0, hashIndex);
                }
                else {
                    alias = colorAndAlias;
                    color = "black";
                }
                String path = buf.substring(commaIndex + 1).trim();
                Shortcut shortcut = new Shortcut(alias, path, color);
                listModel.addElement(shortcut);
            }
            in.close();
        }
        catch (IOException e) {
            APP.error("Unable to load the file " + filePath);
            return null;
        }
        return listModel;
    }


    /**
     * Saves the shortcuts list to a file in user's home directory.
     */
    private void saveShortcuts() {
        String filePath =
            System.getProperty("user.home") + System.getProperty("file.separator")
            + APPLICATION_NAME + ".accessory.dirs";
        try {
            PrintWriter out =
                new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
            out.println("//Directory Shortcuts for " + APPLICATION_NAME + " ["
                + new Date().toString() + ']');
            for (int i = 0; i < model.size(); i++) {
                Shortcut shortcut = (Shortcut)model.get(i);
                String alias = shortcut.getAlias();
                String path = shortcut.getPath();
                String color = shortcut.getColorString();
                out.println(color + '#' + alias + ',' + path);
            }
            out.close();
        }
        catch (IOException e) {
            APP.error("Unable to save the file " + filePath);
            return;
        }
    }


    /**
     * Displays the current directory path in the title bar of JFileChooser.
     */
    private void updateTitle() {
        if (initialTitle == null) {
            //chooser.getDialogTitle() returns null, so the title is retrieved from UI
            initialTitle = chooser.getUI().getDialogTitle(chooser);
        }
        chooser.setDialogTitle(initialTitle + " ("
            + chooser.getCurrentDirectory().getPath() + ")");
    }
}



/**
 * This class defines a shortcut object in the list.
 */
class Shortcut {
    private String alias;
    private String path;
    private Color color;

    Shortcut(String alias, String path, String color) {
        this.alias = alias;
        this.path = path;
        this.color = parseColor(color);
    }

    public boolean hasAlias() {
        return (alias.length() > 0);
    }


    public String getAlias() {
        return alias;
    }


    public void setAlias(String newAlias) {
        alias = newAlias;
    }


    public String getPath() {
        return path;
    }


    public String getName() {
        if (hasAlias()) {
            return alias;
        }
        return path;
    }


    /**
     * Formats shortcut's name for display. This method can be modified to meet other
     * display format expectations.
     *
     * @return path
     */
    public String getDisplayName() {
        if (hasAlias()) {
            return '[' + alias + ']';
        }
        return path;
    }


    public Color getColor() {
        return color;
    }


    public void setColor(String color) {
        this.color = parseColor(color);
    }


    public String getColorString() {
        return colorToString(color);
    }


    /**
     * Converts color to string. Some colors defined in Color are used as is (for
     * instance, Color.blue). Green, teal and yellow colors are defined in this method.
     * Other colors are represented as an RGB hexadecimal string (without  the alpha
     * component).
     *
     * @param col couleur à traduire
     *
     * @return retourne la traduction de la couleur
     */
    private String colorToString(Color col) {
        if (col == Color.blue) {
            return "blue";
        }
        else if (col == Color.cyan) {
            return "cyan";
        }
        else if (col == Color.gray) {
            return "gray";
        }
        else if (col == Color.magenta) {
            return "magenta";
        }
        else if (col == Color.orange) {
            return "orange";
        }
        else if (col == Color.pink) {
            return "pink";
        }
        else if (col == Color.red) {
            return "red";
        }
        else if (col == Color.black) {
            return "black";
        }
        String fullColorStr = Integer.toHexString(col.getRGB());

        //The first two digits in fullColorStr are ignored in colorStr (alpha component)
        String colorStr = fullColorStr.substring(2);
        if ("339933".equals(colorStr)) {
            return "green";
        }
        else if ("cccc33".equals(colorStr)) {
            return "yellow";
        }
        else if ("66cc99".equals(colorStr)) {
            return "teal";
        }
        return colorStr;
    }


    private Color parseColor(String colorString) {
        try {
            int rgb = Integer.parseInt(colorString, 16);
            return new Color(rgb);
        }
        catch (NumberFormatException e) {
            if ("blue".equals(colorString)) {
                return Color.blue;
            }
            if ("cyan".equals(colorString)) {
                return Color.cyan;
            }
            if ("gray".equals(colorString)) {
                return Color.gray;
            }
            if ("green".equals(colorString)) {
                return new Color(0x33, 0x99, 0x33);
            }
            if ("magenta".equals(colorString)) {
                return Color.magenta;
            }
            if ("orange".equals(colorString)) {
                return Color.orange;
            }
            if ("pink".equals(colorString)) {
                return Color.pink;
            }
            if ("red".equals(colorString)) {
                return Color.red;
            }
            if ("teal".equals(colorString)) {
                return new Color(0x66, 0xcc, 0x99);
            }
            if ("yellow".equals(colorString)) {
                return new Color(0xcc, 0xcc, 0x33);
            }
            return Color.black;
        }
    }


    @Override
    public String toString() {
        return "[" + alias + "," + path + "," + colorToString(color) + "]";
    }
}
