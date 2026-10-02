import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Genera un fichero con 100.000 líneas de 60 letras
 * aleatorias en mayúscula de la A a la Z.
 *
 * Cada ejecución de esta clase corresponde a un proceso
 * independiente lanzado desde la clase Colaborar.
 *
 * @author Francisco Ortiz
 * @version 1.0
 * @since 29/09/2026
 */
public class Lenguaje {

    // Número de líneas que tendrá cada fichero
    private static final int NUM_LINEAS = 100000;

    // Número de letras que tendrá cada línea
    private static final int LETRAS_LINEA = 60;

    /**
     * Método principal.
     *
     * Recibe como argumento el nombre del fichero
     * que debe generar.
     *
     * @param args contiene el nombre del fichero de salida
     */
    public static void main(String[] args) {

        // Comprobamos que se haya recibido
        // el nombre del fichero
        if (args.length == 0) {
            System.out.println("No se ha indicado el nombre del fichero.");

            // Terminamos indicando que ha ocurrido un error
            System.exit(1);
        }
        // Guardamos el nombre del fichero recibido
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

                    /*
                     * Generamos un número entre 0 y 25
                     * y lo sumamos al código de la letra 'A'.
                     *
                     * De esta forma obtenemos una letra
                     * aleatoria entre A y Z.
                     */
                    char letra = (char) ('A' + random.nextInt(26));

                    // Escribimos la letra en el fichero
                    escritor.write(letra);
                }

                // Después de las 60 letras,
                // hacemos un salto de línea
                escritor.write("\n");
            }

            // Cerramos el fichero
            escritor.close();

            // El proceso termina correctamente
            System.exit(0);

        } catch (IOException e) {

            // Mostramos el error si no se puede escribir
            System.out.println("Error al escribir el fichero: " + e.getMessage());

            // Indicamos que el proceso ha terminado con error
            System.exit(1);
        }
    }
}