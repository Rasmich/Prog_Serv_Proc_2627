import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class HablarConElHijo {
    static void main(String[] args) throws IOException {
        Process hijo = new ProcessBuilder("sort").start();
        String[] palabras = {"pera","manzana","kiwi", "cereza","chirimolla","Aguacate marino"};
        //getoutputStream()
        try (PrintWriter haciaElHijo = new PrintWriter(hijo.getOutputStream())){
            for (String palabra : palabras) {
                System.out.println("    ->" + palabra);
                haciaElHijo.println(palabra);

            }
        }

        //getInputStream() es nuestra entrada y la salida del hijo
        try (BufferedReader desdeElHijo = new BufferedReader(
                new InputStreamReader(hijo.getInputStream()))) {
            String linea;
            while ((linea = desdeElHijo.readLine()) != null){
                System.out.println("  <-" + linea);

            }
        }



    }
}
