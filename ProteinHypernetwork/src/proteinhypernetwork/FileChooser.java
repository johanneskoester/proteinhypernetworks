/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetwork;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FileChooser {

  private static Frame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
  private static JFileChooser f = new JFileChooser();
  private static Preferences prefs = Preferences.userNodeForPackage(ProteinHypernetworkApp.class);
  private static String LASTPATH = "lastpath";

  private static FileFilter filter = null;

  public static void setFileFilter(FileFilter filter) {
    FileChooser.filter = filter;
  }

  public static File openFile() {
    if (false && ProteinHypernetworkApp.MAC_OS_X) {
      return nativeDialog(FileDialog.LOAD);
    } else {
      if(filter != null)
        f.setFileFilter(filter);
      filter = null;
      f.setCurrentDirectory(new File(prefs.get(LASTPATH, System.getProperty("user.home"))));
      if (f.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
        prefs.put(LASTPATH, f.getSelectedFile().getParent());
        return f.getSelectedFile();
      } else {
        return null;
      }
    }
  }

  public static File openFolder() {
    if (false && ProteinHypernetworkApp.MAC_OS_X) {
      System.setProperty("apple.awt.fileDialogForDirectories", "true");
      File dir = nativeDialog(FileDialog.LOAD);
      System.setProperty("apple.awt.fileDialogForDirectories", "false");
      return dir;
    } else {
      if(filter != null)
        f.setFileFilter(filter);
      filter = null;
      f.setCurrentDirectory(new File(prefs.get(LASTPATH, System.getProperty("user.home"))));
      f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      if (f.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
        prefs.put(LASTPATH, f.getSelectedFile().getParent());
        return f.getSelectedFile();
      } else {
        return null;
      }
    }
  }

  public static File saveFile() {
    if (false && ProteinHypernetworkApp.MAC_OS_X) {
      return nativeDialog(FileDialog.SAVE);
    } else {
      JFileChooser f = new JFileChooser();
      if(filter != null)
        f.setFileFilter(filter);
      filter = null;
      f.setCurrentDirectory(new File(prefs.get(LASTPATH, System.getProperty("user.home"))));
      if (f.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
        prefs.put(LASTPATH, f.getSelectedFile().getParent());
        return f.getSelectedFile();
      } else {
        return null;
      }
    }
  }

  private static File nativeDialog(int mode) {
    FileDialog f = new FileDialog(parent);
    
    f.setMode(mode);
    f.show();
    if (f.getFile() != null) {
      return new File(f.getDirectory() + "/" + f.getFile());
    } else {
      return null;
    }
  }
}
