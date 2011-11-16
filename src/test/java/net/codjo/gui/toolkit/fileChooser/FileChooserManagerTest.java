package net.codjo.gui.toolkit.fileChooser;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import org.uispec4j.TabGroup;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.FileChooserHandler;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.utils.ComponentUtils;
/**
 *
 */
public class FileChooserManagerTest extends UISpecTestCase {
    private FileChooserManager manager;


    public void test_showChooserForOpen() throws Exception {
        assertTrue(manager.isWithAccessories());

        manager.setWithAccessories(false);
        manager.setTitle("Window title");

        initShowChooserForOpen().process(new WindowHandler() {

            @Override
            public Trigger process(final Window window) throws Exception {
                Component[] components = window.getSwingComponents(JFileChooser.class);
                assertEquals(1, components.length);

                JFileChooser jFileChooser = (JFileChooser)components[0];

                window.assertTitleEquals("Window title");
                assertNull(jFileChooser.getAccessory());

                return new Trigger() {
                    public void run() throws Exception {
                        ComponentUtils.close(window);
                    }
                };
            }
        }).run();
    }


    public void test_showChooserForOpen_withAccessories() throws Exception {

        initShowChooserForOpen().process(new WindowHandler() {
            @Override
            public Trigger process(final Window window) throws Exception {
                Component[] components = window.getSwingComponents(JFileChooser.class);
                JFileChooser jFileChooser = (JFileChooser)components[0];

                TabGroup group = new TabGroup((JTabbedPane)jFileChooser.getAccessory());

                assertTrue(group.tabNamesEquals(new String[]{" Favoris ", " Recherche ", " Visualisation "}));

                return new Trigger() {
                    public void run() throws Exception {
                        ComponentUtils.close(window);
                    }
                };
            }
        }).run();
    }


    public void test_showChooserForOpen_selectionMode() throws Exception {
        initShowChooserForOpen()
              .process(new FileChooserHandler()
                    .assertAcceptsFilesAndDirectories()
                    .cancelSelection());

        manager.setSelectionMode(JFileChooser.FILES_ONLY);

        initShowChooserForOpen()
              .process(new FileChooserHandler()
                    .assertAcceptsFilesOnly()
                    .cancelSelection());

        manager.setSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        initShowChooserForOpen()
              .process(new FileChooserHandler()
                    .assertAcceptsDirectoriesOnly()
                    .cancelSelection());
    }


    private WindowInterceptor initShowChooserForOpen() {
        return WindowInterceptor.init(new Trigger() {
            public void run() throws Exception {
                manager.showChooserForOpen();
            }
        });
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        manager = new FileChooserManager();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
