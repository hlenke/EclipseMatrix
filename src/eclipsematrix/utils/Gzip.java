package eclipsematrix.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
    public static void zipFiles(String args[]) {
        int read = 0;
        byte[] data = new byte[1024];
        // Jeden übergebenen Dateinamen bearbeiten
        for (int i=0; i < args.length; i++) {
          try {
            // Original-Datei mit Stream verbinden
            File f = new File(args[i]);
            FileInputStream in = new FileInputStream(f);
            // Ausgabedatei erstellen
            GZIPOutputStream out =
              new GZIPOutputStream(
                new FileOutputStream(args[i]+".gz"));
            // Alle Daten der Original-Datei in die Ausgabedatei schreiben
            while((read = in.read(data, 0, 1024)) != -1)
              out.write(data, 0, read);
            in.close();
            out.close();
            f.delete();   // Original-Datei löschen
          }
          catch(Exception e) {
            e.printStackTrace();
          }
        }
      }
}
