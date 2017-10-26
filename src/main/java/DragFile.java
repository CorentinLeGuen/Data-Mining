import java.io.File;

public class DragFile {


    public static void listFilesInFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println(fileEntry.getName()+"/");
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
