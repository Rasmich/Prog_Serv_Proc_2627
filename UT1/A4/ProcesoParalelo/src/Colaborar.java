import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lanza 4 procesos Lenguaje de dos formas, secuencial y en paralelo,
 * y compara el tiempo que tarda cada una.
 *
 * @author Francisco Ortiz
 * @version 1.0
 * @since 29/09/2026
 */
public class Colaborar {

    // Número de procesos que vamos a lanzar
    private static final int NUM_PROCESOS = 4;

    // Número de líneas que esperamos encontrar en cada fichero
    private static final int LINEAS_ESPERADAS = 100000;

    /**
     * Método principal del programa.
     * Ejecuta los dos modos, muestra los tiempos y comprueba los ficheros.
     *
     * @param args argumentos del programa
     */
    public static void main(String[] args) {

        try {

            // Ejecutamos los 4 procesos de forma secuencial
            // y guardamos el tiempo total
            long tiempoSecuencial = ejecutarSecuencial();

            // Ejecutamos los 4 procesos de forma paralela
            // y guardamos el tiempo total
            long tiempoParalelo = ejecutarParalelo();

            // Calculamos cuántas veces ha mejorado el tiempo
            double mejora = (double) tiempoSecuencial / tiempoParalelo;

            // Obtenemos el número de procesadores disponibles
            int nucleos = Runtime.getRuntime().availableProcessors();

            // Mostramos los resultados
            System.out.println("\n----- RESULTADOS -----");

            System.out.println("Secuencial: " + tiempoSecuencial + " ms");
            System.out.println("Paralelo: " + tiempoParalelo + " ms");
            System.out.printf("Mejora: %.2f veces%n", mejora);
            System.out.println("Núcleos disponibles: " + nucleos);

            // Comprobamos los ficheros creados en modo secuencial
            System.out.println("\n----- FICHEROS SECUENCIALES -----");

            for (int i = 1; i <= NUM_PROCESOS; i++) {

                String nombreFichero = "seq" + i + ".txt";

                int lineas = contarLineas(nombreFichero);

                // Comprobamos que tenga exactamente 100.000 líneas
                if (lineas == LINEAS_ESPERADAS) {
                    System.out.println(nombreFichero + ": "
                            + lineas + " líneas - CORRECTO");
                } else {
                    System.out.println(nombreFichero + ": "
                            + lineas + " líneas - ERROR");
                }
            }

            // Comprobamos los ficheros creados en modo paralelo
            System.out.println("\n----- FICHEROS PARALELOS -----");

            for (int i = 1; i <= NUM_PROCESOS; i++) {

                String nombreFichero = "par" + i + ".txt";

                int lineas = contarLineas(nombreFichero);

                // Comprobamos que tenga exactamente 100.000 líneas
                if (lineas == LINEAS_ESPERADAS) {
                    System.out.println(nombreFichero + ": "
                            + lineas + " líneas - CORRECTO");
                } else {
                    System.out.println(nombreFichero + ": "
                            + lineas + " líneas - ERROR");
                }
            }

        } catch (IOException e) {

            // Error relacionado con los ficheros o procesos
            System.out.println("Error: " + e.getMessage());

        } catch (InterruptedException e) {

            // Error si alguno de los procesos es interrumpido
            System.out.println("Se ha interrumpido un proceso: "
                    + e.getMessage());
        }
    }


    /**
     * Lanza un proceso Lenguaje indicando el fichero donde debe escribir.
     *
     * @param nombreFichero nombre del fichero de salida
     * @return proceso que se ha lanzado
     * @throws IOException si ocurre un error al iniciar el proceso
     */
    private static Process lanzar(String nombreFichero)
            throws IOException {

        // Obtenemos la ruta del ejecutable de Java
        String java = System.getProperty("java.home")
                + "\\bin\\java.exe";

        // Creamos el ProcessBuilder indicando:
        // 1. El ejecutable de Java
        // 2. El classpath de la aplicación
        // 3. La clase Lenguaje
        // 4. El nombre del fichero
        ProcessBuilder pb = new ProcessBuilder(
                java,
                "-cp",
                System.getProperty("java.class.path"),
                Lenguaje.class.getName(),
                nombreFichero
        );

        // Iniciamos el proceso y lo devolvemos
        return pb.start();
    }


    /**
     * Ejecuta los 4 procesos de forma secuencial.
     * Cada proceso debe terminar antes de lanzar el siguiente.
     *
     * @return tiempo total en milisegundos
     * @throws InterruptedException si se interrumpe un proceso
     * @throws IOException si ocurre un error al lanzar un proceso
     */
    private static long ejecutarSecuencial()
            throws InterruptedException, IOException {

        // Guardamos el momento de inicio
        long inicio = System.nanoTime();

        // Lanzamos los procesos uno detrás de otro
        for (int i = 1; i <= NUM_PROCESOS; i++) {

            // Lanzamos el proceso indicando su fichero
            Process proceso = lanzar("seq" + i + ".txt");

            // Esperamos a que termine antes de lanzar el siguiente
            int codigo = proceso.waitFor();

            // Comprobamos que haya terminado correctamente
            if (codigo != 0) {

                System.out.println(
                        "El proceso " + i
                                + " ha fallado (código " + codigo + ")"
                );
            }
        }

        // Calculamos el tiempo total y lo pasamos a milisegundos
        return (System.nanoTime() - inicio) / 1_000_000;
    }


    /**
     * Ejecuta los 4 procesos de forma paralela.
     * Primero los lanza todos y después espera a que terminen.
     *
     * @return tiempo total en milisegundos
     * @throws InterruptedException si se interrumpe un proceso
     * @throws IOException si ocurre un error al lanzar un proceso
     */
    private static long ejecutarParalelo()
            throws InterruptedException, IOException {

        // Guardamos el momento de inicio
        long inicio = System.nanoTime();

        // Creamos una lista para guardar los procesos
        List<Process> procesos = new ArrayList<>();

        // Primer bucle: lanzamos los 4 procesos sin esperar
        for (int i = 1; i <= NUM_PROCESOS; i++) {

            Process proceso = lanzar("par" + i + ".txt");

            // Guardamos el proceso en la lista
            procesos.add(proceso);
        }

        // En este punto los 4 procesos ya han sido lanzados

        // Segundo bucle: esperamos a que terminen
        for (Process proceso : procesos) {

            int codigo = proceso.waitFor();

            // Comprobamos que el proceso haya terminado correctamente
            if (codigo != 0) {

                System.out.println(
                        "Un proceso paralelo ha fallado (código "
                                + codigo + ")"
                );
            }
        }

        // Calculamos el tiempo total y lo pasamos a milisegundos
        return (System.nanoTime() - inicio) / 1_000_000;
    }


    /**
     * Cuenta el número de líneas que contiene un fichero.
     *
     * @param nombreFichero nombre del fichero que queremos comprobar
     * @return número de líneas encontradas
     * @throws IOException si ocurre un error al leer el fichero
     */
    private static int contarLineas(String nombreFichero)
            throws IOException {

        // Abrimos el fichero para leerlo línea por línea
        BufferedReader lector =
                new BufferedReader(new FileReader(nombreFichero));

        // Variable donde guardamos el número de líneas
        int contador = 0;

        // Leemos hasta llegar al final del fichero
        while (lector.readLine() != null) {

            // Por cada línea aumentamos el contador
            contador++;
        }

        // Cerramos el fichero
        lector.close();

        // Devolvemos el número total de líneas
        return contador;
    }
}