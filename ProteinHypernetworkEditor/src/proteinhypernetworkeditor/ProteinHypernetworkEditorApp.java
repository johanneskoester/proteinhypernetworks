/*
 * ProteinHypernetworkEditorApp.java
 */
package proteinhypernetworkeditor;

import controller.Controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ProteinHypernetworkEditorApp extends SingleFrameApplication {

  public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));

  public static String open;
  private static int randomStep;
  private static int randomNum;
  private static String importConstraints;

  public void setupOSXEnvironment() {
    if (MAC_OS_X) {
      
      try {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
        // use as delegates for various com.apple.eawt.ApplicationListener methods
        OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[]) null));
        OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[]) null));
        //OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
        //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
      } catch (Exception e) {
        System.err.println("Error while loading the OSXAdapter:");
        e.printStackTrace();
      }
    }
  }

  public static void quit() {
    Application.getInstance().exit();
  }

  public static void about() {
    ((ProteinHypernetworkEditorView) ((ProteinHypernetworkEditorApp) Application.getInstance()).getMainView()).showAboutBox();
  }

  /**
   * At startup create and show the main frame of the application.
   */
  @Override
  protected void startup() {
    setupOSXEnvironment();
    if(randomStep > 0) {
      try {
        Controller.getInstance().loadFrom(new File(open));
        Controller.getInstance().removeRandomConstraints(randomStep, randomNum);
      } catch (XMLStreamException ex) {
        Logger.getLogger(ProteinHypernetworkEditorApp.class.getName()).log(Level.SEVERE, null, ex);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(ProteinHypernetworkEditorApp.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else if(open != null && importConstraints != null) {
      try {
        Controller.getInstance().loadFrom(new File(open));
        Controller.getInstance().importConstraintsFromCsv(new File(importConstraints));
        Controller.getInstance().save();
      } catch (Exception ex) {
        Logger.getLogger(ProteinHypernetworkEditorApp.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else {
      show(new ProteinHypernetworkEditorView(ProteinHypernetworkEditorApp.this));
    }
  }

  @Override
  protected void initialize(String[] args) {
    super.initialize(args);
    for (String arg : args) {
      if(arg.startsWith("--random=")) {
        String[] par = arg.substring(9).split(",");
        randomStep = Integer.parseInt(par[0]);
        randomNum = Integer.parseInt(par[1]);
      }
      else if(arg.startsWith("--open=")) {
        open = arg.substring(7);
      }
      else if(arg.startsWith("--importConstraints=")) {
        importConstraints = arg.substring(20);
      }
    }
  }

  /**
   * This method is to initialize the specified window by injecting resources.
   * Windows shown in our application come fully initialized from the GUI
   * builder, so this additional configuration is not needed.
   */
  @Override
  protected void configureWindow(java.awt.Window root) {
  }

  /**
   * A convenient static getter for the application instance.
   * @return the instance of ProteinHypernetworkEditorApp
   */
  public static ProteinHypernetworkEditorApp getApplication() {
    return Application.getInstance(ProteinHypernetworkEditorApp.class);
  }

  /**
   * Main method launching the application.
   */
  public static void main(String[] args) {
    launch(ProteinHypernetworkEditorApp.class, args);
  }
}
