package net.codjo.gui.toolkit.text;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class DocumentWithMaxSizeService {
    private DocumentWithMaxSizeService() {
    }


    public static void install(JTextComponent textComponent, int maxTextLength) {
        Document oldDocument = textComponent.getDocument();
        Document newDocument = new DocumentWrapper(oldDocument, maxTextLength);
        textComponent.setDocument(newDocument);
    }


    static class DocumentWrapper implements Document {
        private final Document document;
        private int maxTextLength = -1;


        DocumentWrapper(Document document, int maxTextLength) {
            this.document = document;
            this.maxTextLength = maxTextLength;
        }


        public void insertString(int offset, String str, AttributeSet attributeSet)
              throws BadLocationException {
            if (str == null) {
                return;
            }

            if (maxTextLength != -1) {
                int remainingChars = maxTextLength - getLength();
                str = str.substring(0, Math.min(str.length(), remainingChars));
            }

            document.insertString(offset, str, attributeSet);
        }


        public int getLength() {
            return document.getLength();
        }


        public void addDocumentListener(DocumentListener listener) {
            document.addDocumentListener(listener);
        }


        public void removeDocumentListener(DocumentListener listener) {
            document.removeDocumentListener(listener);
        }


        public void addUndoableEditListener(UndoableEditListener listener) {
            document.addUndoableEditListener(listener);
        }


        public void removeUndoableEditListener(UndoableEditListener listener) {
            document.removeUndoableEditListener(listener);
        }


        public Object getProperty(Object key) {
            return document.getProperty(key);
        }


        public void putProperty(Object key, Object value) {
            document.putProperty(key, value);
        }


        public void remove(int offs, int len) throws BadLocationException {
            document.remove(offs, len);
        }


        public String getText(int offset, int length) throws BadLocationException {
            return document.getText(offset, length);
        }


        public void getText(int offset, int length, Segment txt) throws BadLocationException {
            document.getText(offset, length, txt);
        }


        public Position getStartPosition() {
            return document.getStartPosition();
        }


        public Position getEndPosition() {
            return document.getEndPosition();
        }


        public Position createPosition(int offs) throws BadLocationException {
            return document.createPosition(offs);
        }


        public Element[] getRootElements() {
            return document.getRootElements();
        }


        public Element getDefaultRootElement() {
            return document.getDefaultRootElement();
        }


        public void render(Runnable runnable) {
            document.render(runnable);
        }
    }
}