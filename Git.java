import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Git {

    public static void main(String[] args) {
        init();
        try {
            System.out.println("== Testing fileHash ==");
            System.out.println(hashFile("test.txt"));
            System.out.println("== Testing createBlob ==");
            createBlob("test.txt");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

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

    public static String hashFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.isRegularFile(path)) {
            throw new IOException("no such file: " + filePath);
        }
        byte[] fileBytes = Files.readAllBytes(path);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 is not available", e);
        }

        byte[] hash = digest.digest(fileBytes);
        return HexFormat.of().formatHex(hash);
    }

    public static void createBlob(String filePath) throws IOException {
        try {
            String hash = hashFile(filePath);
            // No need to check whether file exists because hashFile checks that
            Path path = Path.of(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            Path newObject = Path.of("git/objects/" + hash);
            Files.write(newObject, fileBytes);
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

    }
}
