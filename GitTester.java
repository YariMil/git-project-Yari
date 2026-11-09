import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
            // System.out.println("Commit: " + hash);
            // bw = new BufferedWriter(new FileWriter("myProgram/Test/test.txt", true));
            // bw.write("I am STILL testing this!");
            // bw.close();
            // GitWrapper.add("myProgram/Test/test.txt");
            // GitWrapper.add("myProgram/Hello.txt");
            hash = GitWrapper.commit("John Doe", "Second commit");
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
