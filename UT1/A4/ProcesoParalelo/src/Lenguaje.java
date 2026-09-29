import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Genera un fichero con 100.000 líneas de 60 letras
 * aleatorias en mayúscula de la A a la Z.
 *
 * @author Francisco Ortiz
 * @version 1.0
 * @since 29/09/2026
 */
public class Lenguaje {

    // Número de líneas que tendrá el fichero
    private static final int NUM_LINEAS = 100000;

    // Número de letras que tendrá cada línea
    private static final int LETRAS_LINEA = 60;

    /**
     * Método principal que recibe el nombre del fichero
     * y genera las líneas con letras aleatorias.
     *
     * @param args contiene el nombre del fichero de salida
     */
    public static void main(String[] args) {

        // Comprobamos que se ha recibido el nombre del fichero
        if (args.length == 0) {
            System.out.println("No se ha indicado el nombre del fichero.");
            System.exit(1);
        }

        // Guardamos el nombre recibido
        String nombreFichero = args[0];

        // Creamos el generador de números aleatorios
        Random random = new Random();

        try {

            // Abrimos el fichero para escribir
            FileWriter escritor = new FileWriter(nombreFichero);

            // Generamos las 100.000 líneas
            for (int i = 0; i < NUM_LINEAS; i++) {

                // Generamos las 60 letras de cada línea
                for (int j = 0; j < LETRAS_LINEA; j++) {

                    // Generamos una letra aleatoria entre A y Z
                    char letra = (char) ('A' + random.nextInt(26));

                    escritor.write(letra);
                }

                // Al terminar las 60 letras hacemos un salto de línea
                escritor.write("\n");
            }

            // Cerramos el fichero
            escritor.close();

            // El proceso termina correctamente
            System.exit(0);

        } catch (IOException e) {

            System.out.println("Error al escribir el fichero: " + e.getMessage());

            // Indicamos que el proceso ha terminado con error
            System.exit(1);
        }
    }
}