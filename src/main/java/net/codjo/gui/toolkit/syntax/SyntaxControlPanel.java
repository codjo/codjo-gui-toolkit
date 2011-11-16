/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.syntax;
import net.codjo.expression.SyntaxControl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
/**
 * Panel permettant de vérifier la syntaxe d'une expression située dans un JTextComponent. Pour cela, il
 * suffit de placer ce panel dans la fenêtre où se trouve le JTextComponent de l'expression et de lui
 * positionner ce dernier par la méthode : setExpressionTextComponent.
 *
 * @author LEVEQUT
 */
public class SyntaxControlPanel extends JPanel {
    private static final String DUBBLE_AND = "&&";
    private static final String DUBBLE_OR = "||";
    private ImageIcon yesImage =
          new ImageIcon(SyntaxControlPanel.class.getResource("good.gif"));
    private ImageIcon noImage =
          new ImageIcon(SyntaxControlPanel.class.getResource("bad.gif"));
    private ImageIcon syntaxImage =
          new ImageIcon(SyntaxControlPanel.class.getResource("syntaxVerify.gif"));
    private ImageIcon findImage =
          new ImageIcon(SyntaxControlPanel.class.getResource("loupe.gif"));
    private JScrollPane resultScrollPane = new JScrollPane();
    private JPanel resultPanel = new JPanel();
    private JPanel comaPanel = new JPanel();
    private JLabel checkComaLabel = new JLabel();
    private JButton viewComaButton = new JButton();
    private JLabel comaMsgLabel = new JLabel();
    private JLabel andMsgLabel = new JLabel();
    private JButton viewAndButton = new JButton();
    private JLabel checkAndLabel = new JLabel();
    private JPanel andPanel = new JPanel();
    private JPanel orPanel = new JPanel();
    private JLabel checkOrLabel = new JLabel();
    private JButton viewOrButton = new JButton();
    private JLabel orMsgLabel = new JLabel();
    private JLabel quoteMsgLabel = new JLabel();
    private JPanel quotePanel = new JPanel();
    private JButton viewQuoteButton = new JButton();
    private JLabel checkQuoteLabel = new JLabel();
    private JLabel checkEqualityLabel = new JLabel();
    private JButton viewEqualityButton = new JButton();
    private JLabel equalityMsgLabel = new JLabel();
    private JPanel equalityPanel = new JPanel();
    private JPanel notEqualityPanel = new JPanel();
    private JButton viewNotEqualityButton = new JButton();
    private JLabel checkNotEqualityLabel = new JLabel();
    private JLabel notEqualityMsgLabel = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel verifyPanel = new JPanel();
    private JButton verifySyntaxButton = new JButton();
    private JCheckBox viewComasCheckBox = new JCheckBox();
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private ExpressionCaretListener expCaretListener = new ExpressionCaretListener();
    private int lastEqualityIdx = 0;
    private int lastNotEqualityIdx = 0;
    private int lastQuoteIdx = 0;
    private Map lastLogicalIdx = new HashMap();
    private JTextComponent expressionTextComponent;


    /**
     * Constructeur.
     */
    public SyntaxControlPanel() {
        initIndexes();
        init();
        initListeners();
    }


    /**
     * Positionne le JTextComponent de l'expression à étudier.
     *
     * @param expressionTextComponent Le JTextComponent de l'expression à étudier
     */
    public void setExpressionTextComponent(JTextComponent expressionTextComponent) {
        this.expressionTextComponent = expressionTextComponent;
    }


    /**
     * Retourne le JTextComponent de l'expression à étudier.
     *
     * @return Le JTextComponent de l'expression à étudier
     */
    public JTextComponent getExpressionTextComponent() {
        return this.expressionTextComponent;
    }


    /**
     * Init du Panel
     */
    private void init() {
        this.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
                                                                         new Color(156, 156, 158)),
                                        "Vérificateur de syntaxe"));
        this.setLayout(borderLayout2);

        buildSyntaxPanel(comaPanel, checkComaLabel, viewComaButton, comaMsgLabel);
        buildSyntaxPanel(andPanel, checkAndLabel, viewAndButton, andMsgLabel);
        buildSyntaxPanel(orPanel, checkOrLabel, viewOrButton, orMsgLabel);
        buildSyntaxPanel(quotePanel, checkQuoteLabel, viewQuoteButton, quoteMsgLabel);
        buildSyntaxPanel(equalityPanel, checkEqualityLabel, viewEqualityButton, equalityMsgLabel);
        buildSyntaxPanel(notEqualityPanel, checkNotEqualityLabel, viewNotEqualityButton, notEqualityMsgLabel);
        buildSyntaxPanel(comaPanel, checkComaLabel, viewComaButton, comaMsgLabel);

        verifyPanel.setLayout(borderLayout1);
        verifySyntaxButton.setPreferredSize(new Dimension(40, 35));
        verifySyntaxButton.setIcon(syntaxImage);
        verifySyntaxButton.setToolTipText("Vérifie la syntaxe");
        viewComasCheckBox.setText("Visualisation des parenthèses");

        resultPanel.setLayout(gridBagLayout1);
        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(resultScrollPane, BorderLayout.CENTER);
        resultScrollPane.getViewport().add(resultPanel, null);

        comaPanel.add(checkComaLabel, null);
        comaPanel.add(viewComaButton, null);
        comaPanel.add(comaMsgLabel, null);
        resultPanel.add(comaPanel,
                        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH, new Insets(2, 3, 0, 0), 259, 33));
        andPanel.add(checkAndLabel, null);
        andPanel.add(viewAndButton, null);
        andPanel.add(andMsgLabel, null);
        resultPanel.add(andPanel,
                        new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 259, 33));
        orPanel.add(checkOrLabel, null);
        orPanel.add(viewOrButton, null);
        orPanel.add(orMsgLabel, null);
        resultPanel.add(orPanel,
                        new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 259, 33));
        quotePanel.add(checkQuoteLabel, null);
        quotePanel.add(viewQuoteButton, null);
        quotePanel.add(quoteMsgLabel, null);
        resultPanel.add(quotePanel,
                        new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 259, 33));
        equalityPanel.add(checkEqualityLabel, null);
        equalityPanel.add(viewEqualityButton, null);
        equalityPanel.add(equalityMsgLabel, null);
        resultPanel.add(equalityPanel,
                        new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 259, 33));
        notEqualityPanel.add(checkNotEqualityLabel, null);
        notEqualityPanel.add(viewNotEqualityButton, null);
        notEqualityPanel.add(notEqualityMsgLabel, null);
        resultPanel.add(notEqualityPanel,
                        new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 259, 33));

        this.add(verifyPanel, BorderLayout.SOUTH);
        verifyPanel.add(viewComasCheckBox, BorderLayout.CENTER);
        verifyPanel.add(verifySyntaxButton, BorderLayout.EAST);
    }


    private void buildSyntaxPanel(JPanel syntaxPanel,
                                  JLabel checkLabel,
                                  JButton viewButton,
                                  JLabel msgLabel) {
        syntaxPanel.setLayout(null);
        checkLabel.setBounds(new Rectangle(30, 2, 32, 30));
        viewButton.setBounds(new Rectangle(1, 8, 29, 24));
        viewButton.setBorderPainted(false);
        viewButton.setIcon(findImage);
        viewButton.setVisible(false);
        msgLabel.setBounds(new Rectangle(63, 14, 182, 15));
    }


    private void initListeners() {
        viewEqualityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                viewEqualityError();
            }
        });
        verifySyntaxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                verifySyntax();
            }
        });
        viewComasCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                viewComasChecked();
            }
        });
        viewQuoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                viewQuoteError();
            }
        });
        viewOrButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                viewOrErrorButton();
            }
        });
        viewAndButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                viewAndErrorButton();
            }
        });
        viewNotEqualityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                viewNotEqualityError();
            }
        });
    }


    /**
     * Lance les vérifications de la syntaxe.
     */
    private void verifySyntax() {
        initIndexes();

        String source = expressionTextComponent.getText();
        verifyComas(source);
        verifyAndOpers(source);
        verifyOrOpers(source);
        verifyQuotes(source);
        verifyEqualityOpers(source);
        verifyNotEqualityOpers(source);
    }


    /**
     * Lance la visualisation des parenthèses.
     */
    private void viewComasChecked() {
        if (viewComasCheckBox.isSelected()) {
            expressionTextComponent.addCaretListener(expCaretListener);
        }
        else {
            expressionTextComponent.removeCaretListener(expCaretListener);
        }
    }


    /**
     * Lance la vérification des parenthèses.
     *
     * @param source Le texte de l'expression
     */
    private void verifyComas(String source) {
        int comas = SyntaxControl.checkComas(source);
        if (comas > 1) {
            comaMsgLabel.setText(comas + " parenthèses ouvrantes en trop !");
            comaMsgLabel.setForeground(Color.red);
            checkComaLabel.setIcon(noImage);
        }
        else if (comas == 1) {
            comaMsgLabel.setText("1 parenthèse ouvrante en trop !");
            comaMsgLabel.setForeground(Color.red);
            checkComaLabel.setIcon(noImage);
        }
        else if (comas < -1) {
            int com = -comas;
            comaMsgLabel.setText(com + " parenthèses fermantes en trop !");
            comaMsgLabel.setForeground(Color.red);
            checkComaLabel.setIcon(noImage);
        }
        else if (comas == -1) {
            comaMsgLabel.setText("1 parenthèse fermante en trop !");
            comaMsgLabel.setForeground(Color.red);
            checkComaLabel.setIcon(noImage);
        }
        else {
            comaMsgLabel.setText("Vérification des parenthèses OK");
            comaMsgLabel.setForeground(Color.black);
            checkComaLabel.setIcon(yesImage);
        }
    }


    /**
     * Lance la vérification des opérateurs logiques "&&".
     *
     * @param source Le texte de l'expression
     */
    private void verifyAndOpers(String source) {
        int andOpers = SyntaxControl.checkLogicalOpers(source, DUBBLE_AND);
        if (andOpers > 1) {
            andMsgLabel.setText(andOpers + " caractères & tout seul !");
            andMsgLabel.setForeground(Color.red);
            checkAndLabel.setIcon(noImage);
            viewAndButton.setVisible(true);
        }
        else if (andOpers == 1) {
            andMsgLabel.setText("1 caractère & tout seul !");
            andMsgLabel.setForeground(Color.red);
            checkAndLabel.setIcon(noImage);
            viewAndButton.setVisible(true);
        }
        else {
            andMsgLabel.setText("Vérification des && OK");
            andMsgLabel.setForeground(Color.black);
            checkAndLabel.setIcon(yesImage);
            viewAndButton.setVisible(false);
        }
    }


    /**
     * Lance la vérification des opérateurs logiques "||".
     *
     * @param source Le texte de l'expression
     */
    private void verifyOrOpers(String source) {
        int orOpers = SyntaxControl.checkLogicalOpers(source, DUBBLE_OR);
        if (orOpers > 1) {
            orMsgLabel.setText(orOpers + " caractères | tout seul !");
            orMsgLabel.setForeground(Color.red);
            checkOrLabel.setIcon(noImage);
            viewOrButton.setVisible(true);
        }
        else if (orOpers == 1) {
            orMsgLabel.setText("1 caractère | tout seul !");
            orMsgLabel.setForeground(Color.red);
            checkOrLabel.setIcon(noImage);
            viewOrButton.setVisible(true);
        }
        else {
            orMsgLabel.setText("Vérification des || OK");
            orMsgLabel.setForeground(Color.black);
            checkOrLabel.setIcon(yesImage);
            viewOrButton.setVisible(false);
        }
    }


    /**
     * Lance la vérification des doubles quotes.
     *
     * @param source Le texte de l'expression
     */
    private void verifyQuotes(String source) {
        if (!SyntaxControl.checkQuotes(source)) {
            quoteMsgLabel.setText("Nombre de \" incorrect !");
            quoteMsgLabel.setForeground(Color.red);
            checkQuoteLabel.setIcon(noImage);
            viewQuoteButton.setVisible(true);
        }
        else {
            quoteMsgLabel.setText("Vérification des \"...\" OK");
            quoteMsgLabel.setForeground(Color.black);
            checkQuoteLabel.setIcon(yesImage);
            viewQuoteButton.setVisible(false);
        }
    }


    /**
     * Lance la vérification des opérateurs d'égalité "==".
     *
     * @param source Le texte de l'expression
     */
    private void verifyEqualityOpers(String source) {
        int equalOpers = SyntaxControl.checkEqualityOpers(source);
        if (equalOpers > 1) {
            equalityMsgLabel.setText(equalOpers + " caractères = tout seul !");
            equalityMsgLabel.setForeground(Color.red);
            checkEqualityLabel.setIcon(noImage);
            viewEqualityButton.setVisible(true);
        }
        else if (equalOpers == 1) {
            equalityMsgLabel.setText("1 caractère = tout seul !");
            equalityMsgLabel.setForeground(Color.red);
            checkEqualityLabel.setIcon(noImage);
            viewEqualityButton.setVisible(true);
        }
        else {
            equalityMsgLabel.setText("Vérification des == OK");
            equalityMsgLabel.setForeground(Color.black);
            checkEqualityLabel.setIcon(yesImage);
            viewEqualityButton.setVisible(false);
        }
    }


    /**
     * Lance la vérification des opérateurs d'inégalité "!=".
     *
     * @param source Le texte de l'expression
     */
    private void verifyNotEqualityOpers(String source) {
        int notEqualOpers = SyntaxControl.checkNotEqualityOpers(source);
        if (notEqualOpers > 1) {
            notEqualityMsgLabel.setText(notEqualOpers + " caractères ! tout seul !");
            notEqualityMsgLabel.setForeground(Color.red);
            checkNotEqualityLabel.setIcon(noImage);
            viewNotEqualityButton.setVisible(true);
        }
        else if (notEqualOpers == 1) {
            notEqualityMsgLabel.setText("1 caractère ! tout seul !");
            notEqualityMsgLabel.setForeground(Color.red);
            checkNotEqualityLabel.setIcon(noImage);
            viewNotEqualityButton.setVisible(true);
        }
        else {
            notEqualityMsgLabel.setText("Vérification des != OK");
            notEqualityMsgLabel.setForeground(Color.black);
            viewNotEqualityButton.setVisible(false);
            checkNotEqualityLabel.setIcon(yesImage);
        }
    }


    /**
     * Lance la visualisation des opérateurs d'égalité incohérents.
     */
    private void viewEqualityError() {
        if (SyntaxControl.checkEqualityOpers(expressionTextComponent.getText()) > 0) {
            lastEqualityIdx =
                  SyntaxControl.viewEqualityOperError(expressionTextComponent,
                                                      lastEqualityIdx);
        }
    }


    /**
     * Lance la visualisation des doubles quotes.
     */
    private void viewQuoteError() {
        lastQuoteIdx =
              SyntaxControl.viewQuoteError(expressionTextComponent, lastQuoteIdx);
    }


    /**
     * Lance la visualisation des opérateurs d'inégalité incohérents.
     */
    private void viewNotEqualityError() {
        if (SyntaxControl.checkNotEqualityOpers(expressionTextComponent.getText()) > 0) {
            lastNotEqualityIdx =
                  SyntaxControl.viewNotEqualityOperError(expressionTextComponent,
                                                         lastNotEqualityIdx);
        }
    }


    /**
     * Initialise les indexes de départ pour les différentes visualisations.
     */
    private void initIndexes() {
        lastEqualityIdx = 0;
        lastNotEqualityIdx = 0;
        lastQuoteIdx = 0;
        lastLogicalIdx.put(DUBBLE_OR, 0);
        lastLogicalIdx.put(DUBBLE_AND, 0);
    }


    /**
     * Lance la visualisation des opérateurs "||" incohérents.
     */
    private void viewOrErrorButton() {
        if (SyntaxControl.checkLogicalOpers(expressionTextComponent.getText(), DUBBLE_OR) > 0) {
            lastLogicalIdx =
                  SyntaxControl.viewLogicalOperError(expressionTextComponent, DUBBLE_OR,
                                                     lastLogicalIdx);
        }
    }


    /**
     * Lance la visualisation des opérateurs "&&" incohérents.
     */
    private void viewAndErrorButton() {
        if (SyntaxControl.checkLogicalOpers(expressionTextComponent.getText(), DUBBLE_AND) > 0) {
            lastLogicalIdx =
                  SyntaxControl.viewLogicalOperError(expressionTextComponent, DUBBLE_AND,
                                                     lastLogicalIdx);
        }
    }


    /**
     * Lance la sélection de la partie de texte comprise entre deux parenthèses lorsque le curseur est situé à
     * droite d'une parenthèse.
     */
    private void expressionTextArea() {
        String source = expressionTextComponent.getText();
        int idx = expressionTextComponent.getCaretPosition();
        if (idx > 0) {
            String charBeforeCaret = source.substring((idx - 1), idx);

            if (")".equals(charBeforeCaret)) {
                expressionTextComponent.removeCaretListener(expCaretListener);
                SyntaxControl.selectText(expressionTextComponent, ")", "(", idx, false);
                expressionTextComponent.addCaretListener(expCaretListener);
            }
            else if ("(".equals(charBeforeCaret)) {
                expressionTextComponent.removeCaretListener(expCaretListener);
                SyntaxControl.selectText(expressionTextComponent, "(", ")", idx, true);
                expressionTextComponent.addCaretListener(expCaretListener);
            }
        }
    }


    /**
     * Listener sur la position du curseur dans le JTextArea de l'expression.
     *
     * @author Thierrou
     */
    private class ExpressionCaretListener implements CaretListener {
        public void caretUpdate(CaretEvent event) {
            expressionTextArea();
        }
    }
}
