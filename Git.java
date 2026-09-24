import java.io.File;
import java.io.IOException;

public class Git {

    public void init() {
        try {
            File gitRoot = new File("git");
            boolean rootExists = gitRoot.mkdir();
            File gitObjects = new File("git/objects");
            boolean objectsExists = gitObjects.mkdir();
            File gitIndex = new File("git/index");
            boolean indexExists = gitIndex.createNewFile();
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

    }
}
