import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Flujos {
    static void main(String[] args) throws IOException {
        Process p = new ProcessBuilder("tasklist").start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("[hijo] " + linea);
            }
        }

    }
}
