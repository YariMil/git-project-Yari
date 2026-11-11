import java.io.File;
import java.io.IOException;

public class GitWrapper {

    /**
     * Initializes a new Git repository.
     * Creates git/, git/objects/, git/index, and git/HEAD.
     */
    public static void init() {
        Git.init();
    }

    /**
     * Stages a file for the next commit.
     * Throws IOException if the path does not exist or is a directory.
     * Does nothing if the file's content is already staged for that path.
     *
     * @param filePath The path to the file to stage.
     */
    public static void add(String filePath) throws IOException {
        String[] filePaths = { filePath };
        String[] hashes = new String[1];
        Git.stageFiles(filePaths, hashes);
    }

    /**
     * Creates a commit capturing the current state of the index.
     * Builds trees, writes the commit file, updates HEAD.
     *
     * Commit format:
     * tree: <tree_sha>
     * parent: <parent_sha> (omit line for initial commit)
     * author: <author>
     * date: <date>
     * message: <message>
     *
     * @param author  The author name.
     * @param message The commit message.
     * @return The SHA-1 hash of the new commit.
     */
    public static String commit(String author, String message) {
        try {
            return Git.commit(author, message);
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
            return "";
        }
    }

    public static void checkout(String commitHash) {
        File[] branches = new File("git/refs/heads").listFiles();
        for (File branch : branches) {
            // Checks if the provided string is a branch name
            if (branch.getName().equals(commitHash)) {
                Branches.checkoutBranch(commitHash);
                return;
            }
        }
        // Now checking if it's a commit hash
        File[] commits = new File("git/objects").listFiles();
        for (File object : commits) {
            if (object.getName().equals(commitHash)) {
                Branches.checkoutCommit(commitHash);
            }
        }
    }

    public static void createBranch(String branchName) {
        Branches.createBranch(branchName);
    }

}
