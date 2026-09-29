import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
public class EncadenarProcesos {
    public static void main(String[] args) throws Exception {
        List<Process> cadena = ProcessBuilder.startPipeline(List.of(
                new ProcessBuilder("tasklist"),
                new ProcessBuilder("findstr", "chrome")));
        Process ultimo = cadena.get(cadena.size() - 1);
// Solo se lee la salida del ultimo proceso de la cadena
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(ultimo.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }
}