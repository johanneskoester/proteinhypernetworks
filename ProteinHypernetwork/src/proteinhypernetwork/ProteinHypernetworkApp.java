/*
 * ProteinHypernetworkApp.java
 */
package proteinhypernetwork;

import java.io.File;
import javax.swing.JFrame;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import logicProteinHypernetwork.analysis.complexes.lcma.LCMAComplexPrediction;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ProteinHypernetworkApp extends SingleFrameApplication {

  public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
  private boolean noGUI = false;
  private String input;
  private String output;
  private int threadCount = Runtime.getRuntime().availableProcessors();
  private float mergeSimilarityThreshold = 0.4f;
  private boolean masterSwitches = false;
  private boolean synthetic = false;
  private boolean complexes = false;
  private boolean help = false;
  private Class<? extends SPINComplexPrediction> complexPrediction = LCMAComplexPrediction.class;

  public Class<? extends SPINComplexPrediction> getComplexPrediction() {
    return complexPrediction;
  }

  public void setComplexPrediction(Class<? extends SPINComplexPrediction> complexPrediction) {
    this.complexPrediction = complexPrediction;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public float getMergeSimilarityThreshold() {
    return mergeSimilarityThreshold;
  }

  public void registerForMacOSXEvents() {
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
    ((ProteinHypernetworkView) ((ProteinHypernetworkApp) Application.getInstance()).getMainView()).showAboutBox();
  }

  /**
   * At startup create and show the main frame of the application.
   */
  @Override
  protected void startup() {
    if (help) {
      displayHelp();
      return;
    }
    if (!noGUI) {

      registerForMacOSXEvents();
      JFrame.setDefaultLookAndFeelDecorated(true);
      show(new ProteinHypernetworkView(this));
    } else {
      System.setProperty("java.awt.headless", "true");
      if (!masterSwitches && !complexes) {
        displayHelp();
        return;
      }
      // no gui version
      try {
        Controller.getInstance().loadFrom(new File(input));

        Controller.getInstance().setOutput(new File(output));

        if (masterSwitches) {
          if(synthetic)
            Controller.getInstance().setMasterSwitchesDoSynthetic(true);

          Controller.getInstance().predictMasterSwitches();

          Controller.getInstance().exportMasterSwitchesToTsv();
        } else {
          Controller.getInstance().predictComplexes();
          Controller.getInstance().exportComplexesToTsv();
        }

      } catch (Exception ex) {
        System.err.println(ex);
      }
    }
  }

  private void displayHelp() {
    System.out.println("USAGE: ProteinHypernetwork.jar [--nogui [--complexes|--pis] --input=... --output=...] [options]");
    System.out.println("");
    System.out.println("OPTIONS:");
    System.out.println("--nogui\nStart without graphical user interface. You need to specify --input, --output and exactly one of --complexes or --pis.");
    System.out.println("--input=path\nPath to a .ph.xml file containing the definition of the protein hypernetwork.");
    System.out.println("--output=path\nPath to a .csv file that will contain the output.");
    System.out.println("--complexes\nPredict protein complexes for the given hypernetwork.");
    System.out.println("--pis\nPredict the perturbation impact score for the given hypernetwork.");
    System.out.println("--threads=n\nUse a maximum of n threads. Defaults to number of CPU-cores available.");
    System.out.println("--lcma-omega=w\nUse w as omega (neighbor affinity) parameter in LCMA predictions.");
    System.out.println("--help\nDisplay this help.");
  }

  @Override
  protected void initialize(String[] args) {
    super.initialize(args);
    for (String arg : args) {
      if (arg.equals("--nogui")) {
        noGUI = true;
      }
      if (arg.startsWith("--threads=")) {
        threadCount = Integer.parseInt(arg.substring(10));
      }
      if (arg.startsWith("--input=")) {
        input = arg.substring(8);
      }
      if (arg.startsWith("--output=")) {
        output = arg.substring(9);
      }
      if (arg.startsWith("--lcma-omega=")) {
        mergeSimilarityThreshold = Float.parseFloat(arg.substring(13));
      }
      if (arg.equals("--pis")) {
        masterSwitches = true;
      }
      if (arg.equals("--complexes")) {
        complexes = true;
      }
      if (arg.equals("--help")) {
        help = true;
      }
      if (arg.equals("--synthetic")) {
        synthetic = true;
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
   * @return the instance of ProteinHypernetworkApp
   */
  public static ProteinHypernetworkApp getApplication() {
    return Application.getInstance(ProteinHypernetworkApp.class);
  }

  /**
   * Main method launching the application.
   */
  public static void main(String[] args) {
    launch(ProteinHypernetworkApp.class, args);
  }
}
