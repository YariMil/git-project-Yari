import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Branches {
    public static void restoreHistory(String commit) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("git/objects/" + commit));
            String rootHash = br.readLine();
            rootHash = rootHash.substring(rootHash.indexOf(" ") + 1);
            br.close();
            deleteRepoFiles("myProgram");
            restoreDirectory("", rootHash);
        } catch (Exception e) {
            System.out.println("Error in restoreHistory: " + e.getMessage());
        }
    }

    public static void getIndexFromFile() {

    }

    public static void rewriteIndex() {
        File index = new File("git/index");
        try {
            BufferedWriter indexWriter = new BufferedWriter(new FileWriter(index, true));
            StringBuilder writeToIndex = new StringBuilder();
        } catch (Exception e) {
            System.out.println("Error in rewriteIndex: " + e.getMessage());
        }
    }

    public static String getIndexContents() {
        return "";
    }

    public static void deleteRepoFiles(String dirPath) {
        // Deletes files so they can be refilled by restoreHistory
        File rootDir = new File(dirPath);
        File[] files = rootDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteRepoFiles(files[i].getPath());
            } else {
                files[i].delete();
            }
        }
        rootDir.delete();
    }

    public static void restoreDirectory(String dirPath, String hash) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("git/objects/" + hash));
            while (br.ready()) {
                String line = br.readLine();
                if (line.indexOf("blob") != -1) {
                    // BLOB file here
                    int firstSpace = line.indexOf(" ");
                    int lastSpace = line.lastIndexOf(" ");
                    String fileHash = line.substring(firstSpace + 1, lastSpace);
                    String fileName = line.substring(lastSpace + 1);
                    restoreFile(dirPath + fileName, fileHash);
                } else if (line.indexOf("tree") != -1) {
                    // tree file
                    int firstSpace = line.indexOf(" ");
                    int lastSpace = line.lastIndexOf(" ");
                    String dirHash = line.substring(firstSpace + 1, lastSpace);
                    String dirName = line.substring(lastSpace + 1);
                    File newDir = new File(dirPath + dirName);
                    newDir.mkdir();
                    restoreDirectory(dirPath + dirName + "/", dirHash);
                } else {
                    br.close();
                    throw new IllegalStateException("Something went wrong during tree creation!");
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error in restoreDirectory: " + e.getMessage());
        }

    }

    public static void restoreFile(String path, String fileHash) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("git/objects/" + fileHash));
            Path blobPath = Path.of("git/objects/" + fileHash);
            byte[] blobContents = Files.readAllBytes(blobPath);
            Path filePath = Path.of(path);

            File restoredFile = new File(path);

            restoredFile.createNewFile();
            Files.write(filePath, blobContents);
            br.close();
        } catch (Exception e) {
            System.out.println("Error in restoreFile: " + e.getMessage());
        }
    }

    public static String getCurrentCommitHash() {
        String branchAndHash = Git.getBranchAndHash();
        String[] stringParts = branchAndHash.split(" ");
        return stringParts[1];
    }

    public static String getCurrBranch() {
        String branchAndHash = Git.getBranchAndHash();
        String[] stringParts = branchAndHash.split(" ");
        return stringParts[0];
    }

    public static void createBranch(String newBranch) {
        String currHash = getCurrentCommitHash();
        try {
            BufferedWriter branchWriter = new BufferedWriter(new FileWriter("git/refs/heads/" + newBranch));
            branchWriter.write(currHash);
            BufferedWriter headWriter = new BufferedWriter(new FileWriter("git/HEAD"));
            headWriter.write("ref: refs/heads/" + newBranch);
            branchWriter.close();
            headWriter.close();
        } catch (Exception e) {
            System.out.println("Error in createBranch: " + e.getMessage());
        }

    }

    public static void checkoutBranch(String branch) {
        if (!isCreatedBranch(branch)) {
            throw new IllegalStateException("Branch hasn't been created!");
        }
        try {

        } catch (Exception e ) {
            System.out.println("Error in checkout branch: " + e.getMessage());
        }
    }

    public static boolean isCreatedBranch(String branch) {
        File branchDir = new File("git/refs/heads");
        File[] branches = branchDir.listFiles();
        for (File f : branches) {
            if (f.getName().equals(branch)) {
                return true;
            }
        }
        return false;
    }
}
