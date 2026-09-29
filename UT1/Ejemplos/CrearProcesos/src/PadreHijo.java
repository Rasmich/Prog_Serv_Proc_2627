import java.util.Scanner;


/**
 * Objetivo: ver quién es el proceso padre y quiénes son los hijos.
 * * Ejecutar desde IntelliJ y NO cerrar la ventana: los hijos siguen vivos 20 segundos.
 */
        class PadreEHijos {
            private static final int NUM_HIJOS = 3;

            public static void main(String[] args) throws Exception {
                // ================= 1. EL PADRE =================
                // Este programa Java, al ejecutarse, ya ES un proceso: el proceso PADRE.
                // ProcessHandle.current() es el "mando a distancia" sobre nosotros mismos.
                long pidPadre = ProcessHandle.current().pid();
                System.out.println(" PADRE (este programa Java) -> PID " + pidPadre);

                long inicio = System.currentTimeMillis(); // Lo utilizamos para ver cuanto tiempo se ejecutan
                // ================= 2. LOS HIJOS =================
                for (int i = 1; i <= NUM_HIJOS; i++) {
                    // El comando va troceado: cada argumento es un elemento de la lista.
                    // El hijo escribe un mensaje y despues se queda 20 segundos "ocupado"
                    // (el ping a 127.0.0.1 es solo una forma de hacerle esperar).
                    ProcessBuilder pb = new ProcessBuilder(
                            "cmd"
                            , "/c",
                            "echo --> Soy el HIJO " + i + " y estoy escribiendo en la consola de mi padre"
                                    + " & ping -n 21 127.0.0.1 > nul");
                    // inheritIO(): el hijo usa la MISMA consola que el padre.
                    // Sin esta linea no veriamos nada de lo que escribe el hijo.
                    pb.inheritIO();
                    Process hijo = pb.start(); // <--- AQUI nace el proceso hijo
                    System.out.println(" HIJO " + i + " lanzado -> PID " + hijo.pid()
                            + " (vivo: " + hijo.isAlive() + ")");
                }
                long tardado = System.currentTimeMillis() - inicio;
                // ================= 3. LA FAMILIA =================
                System.out.println("\n El padre ha tardado " + tardado + " ms en lanzar a los "
                        + NUM_HIJOS + " hijos.");
                System.out.println(" No los ha esperado: start() devuelve el control al instante.\n");
                // Java sabe quienes son sus hijos directos:
                System.out.println(" Hijos que el padre reconoce ahora mismo:");
                ProcessHandle.current()
                        .children()
                        .forEach(h -> System.out.println(" - PID " + h.pid()));
                // ================= 4. COMPROBACION =================
                System.out.println("\n AHORA: abre el Administrador de tareas o escribe en una consola");
                System.out.println(" tasklist /FI \"PID eq " + pidPadre + "\"");
                System.out.println(" y busca tambien los PID de los hijos.\n");
                System.out.println(" Pulsa INTRO para que el padre termine...");
                new Scanner(System.in).nextLine();
                System.out.println("\n PADRE terminado. Ojo: los hijos pueden seguir vivos.");
            }
        }



