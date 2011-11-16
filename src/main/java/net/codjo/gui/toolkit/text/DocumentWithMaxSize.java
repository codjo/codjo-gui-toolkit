package net.codjo.gui.toolkit.text;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JDesktopPane;
import net.codjo.gui.toolkit.util.ErrorDialog;
/**
 *
 */
public class DocumentWithMaxSize extends PlainDocument {
    protected int maxSize;
    protected JDesktopPane ourDesktopPane;


    public DocumentWithMaxSize(int maxSizeParameter) {
        maxSize = maxSizeParameter;
    }


    public void setDesktopPane(JDesktopPane ourDesktopPane) {
        this.ourDesktopPane = ourDesktopPane;
    }

    protected void handleDocumentTooLong() {}

    @Override
    public void insertString(int idx, String str, AttributeSet attributeSet)
          throws BadLocationException {
        if (getLength() + str.length() <= maxSize) {
            super.insertString(idx, str, attributeSet);
        } else {
            if (ourDesktopPane != null) {
                    ErrorDialog.show(ourDesktopPane, "Insertion impossible",
                        "Ce champ texte est limité à " + maxSize + " caractères.");
                }
            else {
                handleDocumentTooLong();
            }
        }
    }
}