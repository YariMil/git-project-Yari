import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GitTester {
    public static void main(String[] args) throws IOException {
        GitWrapper.init();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("TestingDirectory/Test/test.txt"));
            bw.write("I am testing this!");
            bw.close();
            GitWrapper.add("TestingDirectory/Test/test.txt");
            GitWrapper.add("TestingDirectory/Hello.txt");
            bw = new BufferedWriter(new FileWriter("TestingDirectory/Test/test.txt", true));
            bw.write("I am STILL testing this!");
            bw.close();
            GitWrapper.add("TestingDirectory/Test/test.txt");
            GitWrapper.add("TestingDirectory/Hello.txt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String hash = GitWrapper.commit("John Doe", "Initial commit");
        System.out.println("Commit: " + hash);

    }
}
