import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GitTester {
    public static void main(String[] args) throws IOException {
        GitWrapper.init();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("myProgram/Test/test.txt"));
            bw.write("I am testing this!");
            bw.close();
            GitWrapper.add("myProgram/Test/test.txt");
            GitWrapper.add("myProgram/Hello.txt");
            String hash = GitWrapper.commit("John Doe", "Initial commit");
            System.out.println("Commit: " + hash);
            bw = new BufferedWriter(new FileWriter("myProgram/Test/test.txt", true));
            bw.write("I am STILL testing this!");
            bw.close();
            GitWrapper.add("myProgram/Test/test.txt");
            GitWrapper.add("myProgram/Hello.txt");
            GitWrapper.add("myProgram/Test/wow.txt");
            hash = GitWrapper.commit("John Doe", "Second commit");
            String currCommit = Branches.getCurrentCommitHash();
            BufferedReader branchReader = new BufferedReader(new FileReader("git/objects/" + currCommit));
            branchReader.readLine();
            String oldCommit = branchReader.readLine();
            oldCommit = oldCommit.substring(oldCommit.indexOf(" ") + 1);
            branchReader.close();
            GitWrapper.checkout(oldCommit);
            GitWrapper.checkout(currCommit);
            // Branches.createBranch("Wow");
            // GitWrapper.commit("John Doe", "This is a commit on a new branch!");
            // System.out.println("Commit: " + hash);
            // bw = new BufferedWriter(new FileWriter("myProgram/Test/newThing.txt"));
            // bw.write("wow");
            // bw.close();
            // GitWrapper.add("myProgram/Test/newThing.txt");
            // hash = GitWrapper.commit("John Doe", "Third commit");
            // GitWrapper.checkout("62d96458fb39a59919576857b9a6c19c577fb6ca");
            // GitWrapper.checkout("000edd348859f6995f307207ff08222fdd1ab447");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
