import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lanza varios procesos Lenguaje de forma secuencial y en paralelo
 * para comparar los tiempos de ejecución.
 * <p>
 * Además, realiza las pruebas con 2, 4 y 8 procesos para comprobar
 * cómo afecta el número de procesos al rendimiento.
 *
 * @author Francisco Ortiz
 * @version 2.0
 * @since 29/09/2026
 */
public class Colaborar {

    // Número de líneas que esperamos encontrar en cada fichero
    private static final int LINEAS_ESPERADAS = 100000;

    /*
     * AMPLIACIÓN EXTRA:
     * Además de la prueba obligatoria con 4 procesos, realizamos
     * pruebas adicionales con 2 y 8 procesos.
     *
     * El objetivo es comparar cómo cambia el rendimiento al aumentar
     * o disminuir el número de procesos y poder razonar los resultados
     * obtenidos con datos reales.
     */
    private static final int[] PRUEBAS_PROCESOS = {2, 4, 8};

    /**
     * Método principal del programa.
     * Realiza las pruebas con 2, 4 y 8 procesos,
     * tanto de forma secuencial como paralela.
     *
     * @param args argumentos del programa
     */
    public static void main(String[] args) {

        try {

            // Obtenemos el número de procesadores disponibles en el equipo
            int nucleos = Runtime.getRuntime().availableProcessors();

            System.out.println("Núcleos disponibles: " + nucleos);

            System.out.println("\n======================================");
            System.out.println("       COMPARACIÓN DE RENDIMIENTO");
            System.out.println("======================================");

            // Recorremos las diferentes pruebas: 2, 4 y 8 procesos
            for (int numProcesos : PRUEBAS_PROCESOS) {

                System.out.println("\n--------------------------------------");
                System.out.println("PRUEBA CON " + numProcesos + " PROCESOS");
                System.out.println("--------------------------------------");

                // Ejecutamos los procesos de forma secuencial
                // y guardamos el tiempo total
                long tiempoSecuencial = ejecutarSecuencial(numProcesos);

                // Ejecutamos los procesos de forma paralela
                // y guardamos el tiempo total
                long tiempoParalelo = ejecutarParalelo(numProcesos);

                // Calculamos la mejora obtenida con la ejecución paralela
                double mejora =
                        (double) tiempoSecuencial / tiempoParalelo;

                // Mostramos los resultados de esta prueba
                System.out.println("\n----- RESULTADOS -----");

                System.out.println("Secuencial: " + tiempoSecuencial + " ms");

                System.out.println("Paralelo: " + tiempoParalelo + " ms");

                System.out.printf("Mejora: %.2f veces%n", mejora);

                // Comprobamos que los ficheros secuenciales
                // se hayan generado correctamente
                System.out.println("\n----- FICHEROS SECUENCIALES -----");

                comprobarFicheros("seq", numProcesos);

                // Comprobamos que los ficheros paralelos
                // se hayan generado correctamente
                System.out.println("\n----- FICHEROS PARALELOS -----");

                comprobarFicheros("par", numProcesos);
            }

        } catch (IOException e) {

            // Error relacionado con los ficheros o procesos
            System.out.println("Error: " + e.getMessage());

        } catch (InterruptedException e) {

            // Error si alguno de los procesos es interrumpido
            System.out.println("Se ha interrumpido un proceso: " + e.getMessage());
        }
    }


    /**
     * Lanza un proceso Lenguaje indicando el fichero
     * donde debe escribir.
     *
     * @param nombreFichero nombre del fichero de salida
     * @return proceso que se ha lanzado
     * @throws IOException si ocurre un error al iniciar el proceso
     */
    private static Process lanzar(String nombreFichero)
            throws IOException {

        // Obtenemos la ruta del ejecutable de Java
        String java = System.getProperty("java.home") + "\\bin\\java.exe";
        /*
         * Creamos el ProcessBuilder indicando:
         *
         * 1. El ejecutable de Java.
         * 2. El classpath de la aplicación.
         * 3. La clase Lenguaje que queremos ejecutar.
         * 4. El nombre del fichero que debe crear.
         */
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", System.getProperty("java.class.path"), Lenguaje.class.getName(), nombreFichero);

        // Iniciamos el proceso y lo devolvemos
        return pb.start();
    }


    /**
     * Ejecuta una cantidad determinada de procesos
     * de forma secuencial.
     * <p>
     * Cada proceso debe terminar antes de lanzar
     * el siguiente.
     *
     * @param numProcesos número de procesos que queremos ejecutar
     * @return tiempo total en milisegundos
     * @throws InterruptedException si se interrumpe un proceso
     * @throws IOException          si ocurre un error al lanzar un proceso
     */
    private static long ejecutarSecuencial(int numProcesos)
            throws InterruptedException, IOException {

        // Guardamos el momento de inicio
        long inicio = System.nanoTime();

        // Lanzamos los procesos uno detrás de otro
        for (int i = 1; i <= numProcesos; i++) {

            /*
             * Incluimos el número total de procesos en el nombre
             * para no mezclar los ficheros de las distintas pruebas.
             *
             * Ejemplo:
             * seq2_1.txt
             * seq4_1.txt
             * seq8_1.txt
             */
            String nombreFichero = "seq" + numProcesos + "_" + i + ".txt";

            // Lanzamos el proceso
            Process proceso = lanzar(nombreFichero);

            /*
             * Esperamos a que termine antes de continuar.
             * Esto hace que la ejecución sea SECUENCIAL.
             */
            int codigo = proceso.waitFor();

            // Comprobamos que haya terminado correctamente
            if (codigo != 0) {

                System.out.println("El proceso " + i + " ha fallado (código " + codigo + ")");
            }
        }

        // Calculamos el tiempo total y lo pasamos
        // de nanosegundos a milisegundos
        return (System.nanoTime() - inicio) / 1_000_000;
    }


    /**
     * Ejecuta una cantidad determinada de procesos
     * de forma paralela.
     * <p>
     * Primero se lanzan todos los procesos y después
     * se espera a que terminen.
     *
     * @param numProcesos número de procesos que queremos ejecutar
     * @return tiempo total en milisegundos
     * @throws InterruptedException si se interrumpe un proceso
     * @throws IOException          si ocurre un error al lanzar un proceso
     */
    private static long ejecutarParalelo(int numProcesos)
            throws InterruptedException, IOException {

        // Guardamos el momento de inicio
        long inicio = System.nanoTime();

        // Lista donde guardaremos todos los procesos lanzados
        List<Process> procesos = new ArrayList<>();

        /*
         * PRIMER BUCLE: Lanzamos todos los procesos sin esperar
         * a que termine ninguno.
         */
        for (int i = 1; i <= numProcesos; i++) {

            String nombreFichero = "par" + numProcesos + "_" + i + ".txt";

            // Lanzamos el proceso
            Process proceso = lanzar(nombreFichero);

            // Guardamos el proceso en la lista
            procesos.add(proceso);
        }

        /*
         * En este punto todos los procesos ya han sido lanzados.
         *
         * Por ejemplo, en la prueba de 8 procesos,
         * los 8 procesos están ejecutándose simultáneamente
         * siempre que el sistema disponga de recursos.
         */


        /*
         * SEGUNDO BUCLE: Ahora esperamos a que todos los procesos terminen.
         */
        for (Process proceso : procesos) {

            int codigo = proceso.waitFor();

            // Comprobamos que haya terminado correctamente
            if (codigo != 0) {
                System.out.println("Un proceso paralelo ha fallado " + "(código " + codigo + ")");
            }
        }
        // Calculamos el tiempo total y lo pasamos
        // de nanosegundos a milisegundos
        return (System.nanoTime() - inicio) / 1_000_000;
    }


    /**
     * Comprueba los ficheros generados en una prueba
     * y verifica que tengan exactamente 100.000 líneas.
     *
     * @param tipo        "seq" para secuencial o "par" para paralelo
     * @param numProcesos número de procesos de la prueba
     * @throws IOException si ocurre un error al leer un fichero
     */
    private static void comprobarFicheros(
            String tipo, int numProcesos) throws IOException {

        // Recorremos todos los ficheros de esta prueba
        for (int i = 1; i <= numProcesos; i++) {

            // Construimos el nombre del fichero
            String nombreFichero = tipo + numProcesos + "_" + i + ".txt";

            // Contamos las líneas que contiene
            int lineas = contarLineas(nombreFichero);

            // Comprobamos que tenga exactamente 100.000 líneas
            if (lineas == LINEAS_ESPERADAS) {
                System.out.println(nombreFichero + ": " + lineas + " líneas - CORRECTO");

            } else {
                System.out.println(nombreFichero + ": " + lineas + " líneas - ERROR");
            }
        }
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
        BufferedReader lector = new BufferedReader(new FileReader(nombreFichero));

        // Variable donde guardamos el número de líneas
        int contador = 0;

        // Leemos hasta llegar al final del fichero
        while (lector.readLine() != null) {
            contador++;
        }

        lector.close();

        // Devolvemos el número total de líneas
        return contador;
    }
}