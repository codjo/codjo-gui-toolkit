package net.codjo.gui.toolkit.util;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import junit.framework.TestCase;

public class GuiUtilTest extends TestCase {
    public void test_createImage_simpleComponent() {
        JComponent simple =
              new JLabel(".") {
                  @Override
                  protected void paintComponent(Graphics graphics) {
                      graphics.setColor(Color.blue);
                      graphics.fillRect(0, 0, 1, 1);
                      graphics.setColor(Color.pink);
                      graphics.fillRect(1, 1, 2, 2);
                  }
              };

        BufferedImage result = GuiUtil.createImage(simple);

        assertEquals(simple.getPreferredSize().width, result.getWidth());
        assertEquals(simple.getPreferredSize().height, result.getHeight());

        assertEquals(Color.blue.getRGB(), result.getRGB(0, 0));
        assertEquals(Color.pink.getRGB(), result.getRGB(2, 2));
    }


    public void test_createImage_composedComponent() {
        JComponent subComponent =
              new JLabel(".") {
                  @Override
                  protected void paintComponent(Graphics graphics) {
                      graphics.setColor(Color.blue);
                      graphics.fillRect(0, 0, 100, 100);
                  }
              };
        JPanel composedComponent = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        composedComponent.add(subComponent);
        composedComponent.add(new JLabel("."));

        BufferedImage result = GuiUtil.createImage(composedComponent);

        assertEquals(composedComponent.getPreferredSize().width, result.getWidth());
        assertEquals(composedComponent.getPreferredSize().height, result.getHeight());

        assertEquals(Color.blue.getRGB(), result.getRGB(0, 0));
    }


    public void test_scaleImage_sameProportion() {
        Image source = createRedGreenBox(30, 20);

        BufferedImage scaledImage = GuiUtil.scaleImage(source, new Dimension(15, 10));
        assertBounds(15, 10, scaledImage);

        assertEquals(Color.RED.getRGB(), scaledImage.getRGB(6, 0));
        assertEquals(Color.GREEN.getRGB(), scaledImage.getRGB(8, 0));
    }


    public void test_scaleImage_Reduce() {
        Image source = createRedGreenBox(40, 2);

        BufferedImage scaledImage = GuiUtil.scaleImage(source, new Dimension(20, 10));
        assertBounds(20, 1, scaledImage);

        assertEquals(Color.RED.getRGB(), scaledImage.getRGB(9, 0));
        assertEquals(Color.GREEN.getRGB(), scaledImage.getRGB(11, 0));
    }


    public void test_scaleImage_Increase() {
        Image source = createRedGreenBox(20, 2);

        BufferedImage scaledImage = GuiUtil.scaleImage(source, new Dimension(40, 40));
        assertBounds(40, 4, scaledImage);

        assertEquals(Color.RED.getRGB(), scaledImage.getRGB(18, 0));
        assertEquals(Color.GREEN.getRGB(), scaledImage.getRGB(22, 0));
    }


    private Image createRedGreenBox(int width, int height) {
        Image source = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = source.getGraphics();
        graphics.setColor(Color.RED);
        graphics.fillRect(0, 0, width / 2, height);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(width / 2, 0, width / 2, height);
        return source;
    }


    private void assertBounds(int width, int height, BufferedImage scaledImage) {
        assertEquals(width, scaledImage.getWidth());
        assertEquals(height, scaledImage.getHeight());
    }
}
