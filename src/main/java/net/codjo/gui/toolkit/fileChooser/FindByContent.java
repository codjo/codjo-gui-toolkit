/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
/**
 * Implements user interface and generates FindFilter for selecting files by content.
 * 
 * <P>
 * <b>WARNING:</B> The FindFilter inner class for this object does not implement an
 * efficient strng search algorithm. Efficiency was traded for code simplicity.
 * </p>
 *
 * @author Thierrou
 * @version $Revision: 1.6 $
 */
class FindByContent extends JPanel implements FindFilterFactory {
    /** Find for the first occurrence of the text in this field. */
    private JTextField contentField = null;
    private JCheckBox ignoreCaseCheck = null;

    /**
     * Constructs a user interface and a FindFilterFactory for searching files containing
     * specified text.
     */
    FindByContent() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Name
        JLabel label = new JLabel("Fichier contient...", SwingConstants.LEFT);
        label.setForeground(Color.black);
        label.setFont(new Font("Helvetica", Font.PLAIN, 10));
        panel.add(label);

        contentField = new JTextField();
        contentField.setForeground(Color.black);
        contentField.setFont(new Font("Helvetica", Font.PLAIN, 10));
        panel.add(contentField);

        // ignore case
        ignoreCaseCheck = new JCheckBox("ignorer la casse", true);
        ignoreCaseCheck.setForeground(Color.black);
        ignoreCaseCheck.setFont(new Font("Helvetica", Font.PLAIN, 9));
        panel.add(ignoreCaseCheck);

        add(panel, BorderLayout.NORTH);
    }

    public JTextField getContentField() {
        return contentField;
    }


    public JCheckBox getIgnoreCaseCheck() {
        return ignoreCaseCheck;
    }


    public FindFilter createFindFilter() {
        return new ContentFilter(contentField.getText(), ignoreCaseCheck.isSelected());
    }

    /**
     * Implements a simple content filter.
     *
     * @author Thierrou
     * @version $Revision: 1.6 $
     */
    class ContentFilter implements FindFilter {
        private String content = null;
        private boolean ignoreCase = true;

        ContentFilter(String content, boolean ignoreCase) {
            this.content = content;
            this.ignoreCase = ignoreCase;
        }

        public boolean accept(File file, FindProgressCallback callback) {
            if (file == null) {
                return false;
            }
            if (file.isDirectory()) {
                return false;
            }
            if ((content == null) || (content.length() == 0)) {
                return true;
            }

            boolean result = false;
            BufferedInputStream in = null;
            try {
                long fileLength = file.length();
                in = new BufferedInputStream(new FileInputStream(file));
                byte[] contentBytes;
                if (ignoreCase) {
                    contentBytes = content.toLowerCase().getBytes();
                }
                else {
                    contentBytes = content.getBytes();
                }
                LocatorStream locator = new LocatorStream(contentBytes);
                long counter = 0;
                int callbackCounter = 20;

                // Only call back every 20 bytes
                int by;
                while ((by = in.read()) != -1) {
                    counter++;
                    int matchChar = by;
                    if (ignoreCase) {
                        matchChar = (int)Character.toLowerCase((char)by);
                    }
                    locator.write(matchChar);

                    // This search could be time consuming, especially since
                    // this algorithm is not exactly the most efficient.
                    // Report progress to search monitor and abort
                    // if method returns false.
                    if (callback != null) {
                        if (--callbackCounter <= 0) {
                            if (!callback.reportProgress(this, file, counter, fileLength)) {
                                return false;
                            }
                            callbackCounter = 20;
                        }
                    }
                }
            }
            catch (LocatedException e) {
                result = true;
            }
            catch (Throwable e) {
                ; // Pas grave ?!?
            }
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                }
                catch (IOException e) {
                    ; // Pas grave ?!?
                }
            }
            return result;
        }

        /**
         * Thrown when a LocatorStream object finds a byte array.
         *
         * @author Thierrou
         * @version $Revision: 1.6 $
         */
        class LocatedException extends IOException {
            LocatedException(String msg) {
                super(msg);
            }


            LocatedException(long location) {
                super(String.valueOf(location));
            }
        }


        /**
         * Locate an array of bytes on the output stream. Throws a LocatedStream
         * exception for every occurrence of the byte array.
         *
         * @author Thierrou
         * @version $Revision: 1.6 $
         */
        class LocatorStream extends OutputStream {
            private byte[] locate = null;
            private List matchMakers = new Vector();
            private long mark = 0;

            LocatorStream(byte[] bytes) {
                locate = bytes;
            }

            public void write(int by) throws IOException {
                if (locate == null) {
                    throw new IOException("NULL locator array");
                }
                if (locate.length == 0) {
                    throw new IOException("Empty locator array");
                }

                long foundAt = -1;

                for (int i = matchMakers.size() - 1; i >= 0; i--) {
                    MatchStream matchStream = (MatchStream)matchMakers.get(i);
                    try {
                        matchStream.write(by);
                    }
                    catch (MatchMadeException e) {
                        foundAt = matchStream.getMark();
                        matchMakers.remove(i);
                    }
                    catch (IOException e) {
                        // Match not made. Remove current matchMaker stream.
                        matchMakers.remove(i);
                    }
                }

                if (by == locate[0]) {
                    MatchStream matchStream = new MatchStream(locate, mark);
                    matchStream.write(by);
                    // This will be accepted
                    matchMakers.add(matchStream);
                }
                mark++;

                if (foundAt >= 0) {
                    throw new LocatedException(foundAt);
                }
            }

            /**
             * Thrown when the bytes written match the byte pattern.
             *
             * @author Thierrou
             * @version $Revision: 1.6 $
             */
            class MatchMadeException extends IOException {
                MatchMadeException(String msg) {
                    super(msg);
                }


                MatchMadeException(long mark) {
                    super(String.valueOf(mark));
                }
            }


            /**
             * Accept "output" as long as it matches a specified array of bytes. Throw a
             * MatchMadeException when the bytes written equals the match array. Throw
             * an IOException when a byte does not match. Ignore everything after a
             * match is made.
             *
             * @author Thierrou
             * @version $Revision: 1.6 $
             */
            class MatchStream extends OutputStream {
                private long mark = -1;
                private int pos = 0;
                private byte[] match = null;
                private boolean matchMade = false;

                MatchStream(byte[] match, long mark) {
                    this.mark = mark;
                    this.match = match;
                }

                public int getPos() {
                    return pos;
                }


                public byte[] getMatch() {
                    return match;
                }


                public boolean isMatchMade() {
                    return matchMade;
                }


                /**
                 * Gets the mark attribute of the MatchStream object
                 *
                 * @return The mark value
                 */
                public long getMark() {
                    return mark;
                }


                public void write(int by) throws IOException {
                    if (matchMade) {
                        return;
                    }
                    if (match == null) {
                        throw new IOException("NULL match array");
                    }

                    if (match.length == 0) {
                        throw new IOException("Empty match array");
                    }

                    if (pos >= match.length) {
                        throw new IOException("No match");
                    }

                    if (by != match[pos]) {
                        throw new IOException("No match");
                    }

                    pos++;
                    if (pos >= match.length) {
                        matchMade = true;
                        throw new MatchMadeException(mark);
                    }
                }
            }
        }
    }
}
