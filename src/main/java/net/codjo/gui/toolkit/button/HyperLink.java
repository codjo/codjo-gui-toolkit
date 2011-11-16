package net.codjo.gui.toolkit.button;
import java.awt.Color;
import static java.awt.Color.BLUE;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.Cursor.getPredefinedCursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

public class HyperLink extends JLabel {
    private List<ActionListener> actionListenerList = new ArrayList<ActionListener>();


    public HyperLink(String text) {
        this();
        setText(text);
    }


    public HyperLink() {
        setForeground(BLUE);
        setCursor(getPredefinedCursor(HAND_CURSOR));

        addMouseListener(new LinkMouseListener(this, actionListenerList));
    }


    @Override
    public void setText(String text) {
        super.setText("<html><u>" + text + "</u></html>");
    }


    public void addActionListener(ActionListener actionListener) {
        this.actionListenerList.add(actionListener);
    }


    public void removeActionListener(ActionListener actionListener) {
        this.actionListenerList.remove(actionListener);
    }


    public void removeAllActionListeners() {
        this.actionListenerList.clear();
    }


    private static class LinkMouseListener extends MouseAdapter {
        private JLabel label;
        private final List<ActionListener> actions;
        private Color oldForeground;


        private LinkMouseListener(JLabel label, List<ActionListener> actions) {
            this.label = label;
            this.actions = actions;
        }


        @Override
        public void mouseClicked(MouseEvent event) {
            for (ActionListener action : actions) {
                action.actionPerformed(new ActionEvent(label, 0, null));
            }
        }


        @Override
        public void mouseEntered(MouseEvent event) {
            oldForeground = label.getForeground();
            label.setForeground(oldForeground.darker().darker());
        }


        @Override
        public void mouseExited(MouseEvent event) {
            label.setForeground(oldForeground);
        }
    }
}