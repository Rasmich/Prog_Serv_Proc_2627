import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CrearProcesos {
    static void main(String[] args) throws IOException, InterruptedException {

        /**DIAPOSITIVA 33**/
        //a) Esquema minimo : Problema -> no se ve la terminal del hijo pb.
       // ProcessBuilder pb = new ProcessBuilder("tasklist");
       // Process p = pb.start();

        //b) el hijo utilizará la misma consola que el padre
        // ProcessBuilder pb = new ProcessBuilder("tasklist");
       //  pb.inheritIO();
       //  Process p = pb.start();
       //  p.waitFor();

        /**DIAPOSITIVA 34**/
        //c)Listar el contenido de un directorio
       /* ProcessBuilder pb = new ProcessBuilder("cmd", "/c","dir");
        pb.directory(new File("C:\\Users\\DAM2\\Documents\\2DAM\\Programación multimedia y dispositivos móviles"));
        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();*/
        /**DIAPOSITIVA 38**/
       /* ProcessBuilder pb = new ProcessBuilder("cmd", "/c","echo %MODO%");
        Map<String, String> entorno = pb.environment();
        entorno.put("MODO", "PRUEBAS");
        pb.inheritIO();
         Process p = pb.start();
         p.waitFor();*/

    }
}
