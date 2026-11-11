import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Branches {
    public static void restoreHistory(String commit) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("git/objects/" + commit));
            String rootHash = br.readLine();
            rootHash = rootHash.substring(rootHash.indexOf(" ") + 1);
            br.close();
            // deleteRepoFiles("myProgram");
            ArrayList<String> oldWorkingList = Git.buildWorkingList();
            File index = new File("git/index");
            index.delete();
            index.createNewFile();
            restoreDirectory("", rootHash);
            checkRepository("myProgram", getIndexContent(), compileWorkingList(oldWorkingList));
        } catch (Exception e) {
            System.out.println("Error in restoreHistory: " + e.getMessage());
        }
    }

    public static String compileWorkingList(ArrayList<String> workingList) {
        // Consolidates working list into a single string
        StringBuilder workingListContents = new StringBuilder();
        for (String s : workingList) {
            workingListContents.append(s + "\n");
        }
        return workingListContents.toString();
    }

    public static String getIndexContent() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("git/index"));
            StringBuilder indexContents = new StringBuilder();
            while (br.ready()) {
                indexContents.append(br.readLine());
            }
            br.close();
            return indexContents.toString();
        } catch (Exception e) {
            System.out.println("Error in getIndexContent: " + e.getMessage());
        }
        return "";
    }

    public static void checkRepository(String dirPath, String indexContent, String oldContent) {
        // Checks the repository for any files that were staged in the newer commit
        // But aren't here in the old commit

        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                checkRepository(files[i].getPath(), indexContent, oldContent);
            } else {
                if (oldContent.indexOf(files[i].getPath()) != -1) {
                    if (indexContent.indexOf(files[i].getPath()) == -1) {
                        // Staged later
                        files[i].delete();
                    }
                }
            }
        }
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
                BufferedWriter indexWriter = new BufferedWriter(new FileWriter("git/index", true));
                String line = br.readLine();
                if (line.indexOf("blob") != -1) {
                    // BLOB file here
                    int firstSpace = line.indexOf(" ");
                    int lastSpace = line.lastIndexOf(" ");
                    String fileHash = line.substring(firstSpace + 1, lastSpace);
                    String fileName = line.substring(lastSpace + 1);
                    restoreFile(dirPath + fileName, fileHash);
                    indexWriter.write(fileHash + " " + "git-project-Yari/" + dirPath + fileName + "\n");
                    indexWriter.close();
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
                    indexWriter.close();
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
            branchWriter.close();
        } catch (Exception e) {
            System.out.println("Error in createBranch: " + e.getMessage());
        }

    }

    public static void checkoutBranch(String branch) {
        if (!isCreatedBranch(branch)) {
            throw new IllegalStateException("Branch hasn't been created!");
        }
        try {
            BufferedWriter headWriter = new BufferedWriter(new FileWriter("git/HEAD"));
            headWriter.write("ref: refs/heads/" + branch);
            headWriter.close();
            String currHash = getCurrentCommitHash();
            restoreHistory(currHash);
        } catch (Exception e) {
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

    public static void checkoutCommit(String commitHash) {
        // Creates a detached head commit
        File gitHead = new File("git/HEAD");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(gitHead));
            bw.write(commitHash);
            bw.close();
            restoreHistory(commitHash);
        } catch (Exception e) {
            System.out.println("Error in checkoutCommit: " + e.getMessage());
        }
    }
}
