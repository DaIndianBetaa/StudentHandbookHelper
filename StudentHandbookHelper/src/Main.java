import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            File Memory = new File("Memory.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(Memory));
            BufferedReader br = new BufferedReader(new FileReader(Memory));
            String line;
            if (!Memory.exists()) {
                Memory.createNewFile();
            }

            if (br.readLine() == null) {
                bw.write("test\n");
                bw.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
