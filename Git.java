import java.io.File;
import java.io.IOException;

public class Git {

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        try {
            File gitRoot = new File("git");
            boolean rootExists = !gitRoot.mkdir();
            File gitObjects = new File("git/objects");
            boolean objectsExists = !gitObjects.mkdir();
            File gitIndex = new File("git/index");
            boolean indexExists = !gitIndex.createNewFile();
            File gitHead = new File("git/HEAD");
            boolean headExists = !gitHead.createNewFile();
            if (rootExists && objectsExists && indexExists && headExists) {
                System.out.println("Git Repository already exists.");
            } else {
                System.out.println("Git Repository created.");
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

    }
}
