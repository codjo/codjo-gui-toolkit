package net.codjo.gui.toolkit;
import net.codjo.gui.toolkit.util.ErrorDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class HelpButton extends JButton {
    private String helpUrl;


    public HelpButton() {
        setName("help");
        setText("");
        setIcon(new ImageIcon(getClass().getResource("help.png")));
        setBorderPainted(false);
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayHelp();
            }
        });
    }


    public String getHelpUrl() {
        return helpUrl;
    }


    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }


    private void displayHelp() {
        try {
            Runtime.getRuntime().exec(new String[]{"explorer.exe", helpUrl});
        }
        catch (IOException e) {
            ErrorDialog.show(this, "Impossible d'afficher l'aide.", e);
        }
    }
}
