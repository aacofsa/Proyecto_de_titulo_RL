package IA;

import Utils.Constantes;
import java.util.*;
import static Utils.ResourceLoader.*;
import static Utils.ResourceSaver.*;

public class BasicRL implements Constantes {

    private final float[][] politicas;
    private final int[][] recompensas;
    private final char[][] escenario;

    private int x, y, estado;

    public BasicRL() {
        this.recompensas = cargarRecompensas();
        this.escenario = cargarEscenario();
        this.politicas = cargarPoliticas();
        this.x = INICIO_X;
        this.y = INICIO_Y;
        this.estado = y * ANCHO + x;
        this.imprimirPolitica();
        this.imprimirEscenario();
        this.imprimirReward();
    }

    public void jugar() {
        int recompensaTotal = 0;
        int i = 1;
        String linea = String.format("ITERACION [0] = (x=%d; y=%d; mov=inicial)",x,y);
        System.out.println(linea);
        //Mientras el agente no llegue a un estado final, va a moverse 
        while (!this.esFinal()) {
            //Obtiene la mejor accion para el estado actual segun la matriz de politicas
            int accion = getMejorAccion(estado);
            //Ejecuta la accion, moviendo al agente y actualiza su estado. Retorna la recompensa/penalizacion obtenida
            int recompensa = ejecutarAccion(accion);
            recompensaTotal += recompensa;
            // Se Muestra el resultado de la iteracion
            linea = String.format("ITERACION [%d] = (x=%d; y=%d; mov=%s)",i,x,y,this.accionStr(accion));
            System.out.println(linea);
            i++;
        }
        String entidad = escenario[y][x] == RECOMPENSA ? "Recompensa":"Fantasma";
        linea = String.format("Terminó en estado %d (%s) con una puntuacion de %d",estado,entidad,recompensaTotal);
        System.out.println(linea);
    }

    public void entrenar(int ciclos) {
        for (int i = 0; i < ciclos; i++) {          
            //Mientras el agente no llegue a un estado final, va a moverse y entrenar
            while (!this.esFinal()) {
                int estadoActual = estado;
                //Obtiene la siguiente accion a ejecutar, 20% aleatorio, 80% la mejor accion segun matriz de politicas
                int accion = getAccion(estado);
                //ejecuta la accion, moviendo al agente y actualiza su estado. Retorna la recompensa/penalizacion obtenida
                int recompensa = this.ejecutarAccion(accion);
                //Obtiene el valor maximo de la matriz para el estado actual (despues de aplicar la accion)
                float maxQ = getMaxValorQ(estado);
                float valorQ = politicas[estadoActual][accion];
                // ecuacion de Bellman (Q-learning)
                politicas[estadoActual][accion] = valorQ + LEARNING_RATE * (recompensa + DISCOUNT_FACTOR * maxQ - valorQ);
            }
            //al terminar el entrenamiento, se reinicia el ambiente
            this.y = INICIO_Y;
            this.x = INICIO_X;
            this.estado = y * ANCHO + x;
        }
        // Guarda la matriz en un fichero
        guardarPoliticas(politicas);
    }

    /**
     * Metodo que hace uso del modelo pre entrenado para llegar a la recompensa. crea ficheros con informacion relevante para creacion de informes
     */
    public void jugarVerbose() {
        int totalReward = 0;
        int i = 1;
        String data = "\\begin{itemize}\n";
        while (!this.esFinal()) {
            int xAnterior = x;
            int yAnterior = y;

            int estadoActual = estado;
            int accion = getMejorAccion(estado);
             int recompensa = ejecutarAccion(accion);
            totalReward += recompensa;
            data = data.concat(
                    String.format(
                            "\t\\item Mov %d; Est. %d = (%d,%d)$\\rightarrow$(%d,%d); Acción = %s; Ptje: %d; Q(%d, %s) = %s\n",
                            i,
                            estadoActual,
                            xAnterior,
                            yAnterior,
                            x,
                            y,
                            this.accionStr(accion),
                            totalReward,
                            estadoActual,
                            this.accionStr(accion),
                            fmt(politicas[estadoActual][accion])
                    )
            );
            i++;

        }
        char casilla = escenario[y][x];
        String entidad = casilla == RECOMPENSA ? "Recompensa" : "Fantasma";
        String linea = String.format(
                "\t\\item Fin; Estado %d; %s; Puntuación final %d\n",
                estado,
                entidad,
                totalReward
        );
        data = data.concat(linea);

        data = data.concat("\\end{itemize}");
        guardarTexto("ejecucion.txt", data);
        System.out.println("Terminó en estado " + estado + " con una puntuacion de " + totalReward);
    }
    /**
     * @param ciclos int Indica la cantidad de ciclos a entrenar
     * @param entrenamientosPrevios int Sirve para indicar cuantos entrenamientos se han hecho anteriormente con la finalidad de guardar el resultado del 
     * ultimo entrenamiento en la tabla correspondiente
     * Metodo de entrenamiento. crea ficheros con informacion relevante para creacion de informes
     */ 
    public void entrenarVerbose(int ciclos, int entrenamientosPrevios) {
        System.out.println("\nEntrenamiento con " + ciclos + " ciclos");
        String data = "";
        for (int i = 0; i < ciclos; i++) {
            int j = 0;
            System.out.println("Entrenamiento : " + (i + 1));
            data = data.concat("\\begin{itemize}\n");

            while (!this.esFinal()) {
                data = data.concat("\t\\item Movimiento: " + (j + 1) + "\n");
                data = data.concat("\t\t\\begin{itemize}\n");
                int estadoActual = estado;
                int accion = getAccion(estado);
                float valorQ = politicas[estadoActual][accion];

                String message = String.format(
                        "\t\t\t\\item Estado actual = %d; Acción = %s;  $Q(%d,%s)$ = %s\n",
                        estadoActual,
                        this.accionStr(accion),
                        estadoActual,
                        this.accionStr(accion),
                        fmt(valorQ)
                );
                data = data.concat(message);
                int recompensa = this.ejecutarAccion(accion);
                float max = getMaxValorQ(estado);

                message = String.format(
                        "\t\t\t\\item Próximo estado = %d; Recompensa = %d; Max $Q(%d)$ = %s\n",
                        estado,
                        recompensa,
                        estado,
                        fmt(max)
                );
                data = data.concat(message);

                politicas[estadoActual][accion] = valorQ + LEARNING_RATE * (recompensa + DISCOUNT_FACTOR * max - valorQ);
                message = String.format(
                        "\t\t\t\\item $Q(%d,%s) = %s + %.2f(%d + %.2fx%s-%s)$\n",
                        estadoActual,
                        this.accionStr(accion),
                        fmt(valorQ),
                        LEARNING_RATE,
                        recompensa,
                        DISCOUNT_FACTOR,
                        fmt(max),
                        fmt(valorQ)
                );
                data = data.concat(message);

                message = String.format(
                        "\t\t\t\\item Nuevo $Q(%d,%s)$ = %s\n",
                        estadoActual,
                        this.accionStr(accion),
                        fmt(politicas[estadoActual][accion])
                );
                data = data.concat(message);
                data = data.concat("\t\t\\end{itemize}\n");
                data = data.concat("\n");

                j++;
            }

            //this.recompensas = cargarRecompensas();
        }
        char casilla = escenario[y][x];
        if (casilla == RECOMPENSA) {
            data = data.concat("\t\\item Casilla final : Recompensa\n");
        } else {
            data = data.concat("\t\\item Casilla final : Fantasma\n");
        }

        data = data.concat("\\end{itemize}");
        this.imprimirPolitica();
        guardarPoliticasLatex("Politicas entrenamiento " + (entrenamientosPrevios + ciclos), politicas);
        guardarTexto("traza.txt", data);
        guardarPoliticas(politicas);
    }

    private void imprimirPolitica() {
        System.out.println("  izq arr der abj");
        for (int i = 0; i < CANTIDAD_ESTADOS; i++) {
            System.out.println(i + " " + Arrays.toString(politicas[i]));
        }
        System.out.println();
    }

    private void imprimirReward() {
        for (int i = 0; i < recompensas.length; i++) {
            System.out.println(i + " " + Arrays.toString(recompensas[i]));
        }
        System.out.println();
    }

    private void imprimirEscenario() {
        for (int i = 0; i < recompensas.length; i++) {
            System.out.println(i + " " + Arrays.toString(escenario[i]));
        }
        System.out.println();
    }

    private int getAccion(int estado) {
        Random rd = new Random();
        //Se obtiene el porcentaje random para decidir que accion tomar (0 a 1)
        float isRandomAccion = rd.nextFloat();
        // si el valor random es menor que epsylon (0.2) entonces escoge un movimiento random
        if (isRandomAccion < EPSYLON) {
            return rd.nextInt(ACCIONES);
        }
        //en caso contrario escoge la mejor accion segun la matriz para el estado proporcionado
        return getMejorAccion(estado);
    }

    private int getMejorAccion(int estado) {
        int accion = 0;
        float max = Integer.MIN_VALUE;
        for (int j = 0; j < ACCIONES; j++) {
            float valor = politicas[estado][j];
            if (valor > max) {
                accion = j;
                max = valor;
            }
        }
        return accion;
    }

    private float getMaxValorQ(int estado) {
        int accion = getMejorAccion(estado);
        return politicas[estado][accion];
    }

    private int ejecutarAccion(int accion) {
        switch (accion) {
            // izquierda
            case 0: {
                return moverIzquierda();
            }
            // arriba
            case 1: {
                return moverArriba();
            }
            // derecha
            case 2: {
                return moverDerecha();
            }
            // abajo
            default: {
                return moverAbajo();
            }
        }
    }

    private int moverIzquierda() {
        if (x > 0 && escenario[y][x - 1] != PARED) {
            x--;
            this.estado = y * ANCHO + x;
            return recompensas[y][x];
        } else {
            return PENALIZACION;
        }
    }

    private int moverArriba() {
        if (y > 0 && escenario[y - 1][x] != PARED) {
            y--;
            this.estado = y * ANCHO + x;
            return recompensas[y][x];
        } else {
            return PENALIZACION;
        }
    }

    private int moverDerecha() {
        if (x < ANCHO - 1 && escenario[y][x + 1] != PARED) {
            x++;
            this.estado = y * ANCHO + x;
            return recompensas[y][x];
        } else {
            return PENALIZACION;
        }
    }

    private int moverAbajo() {
        if (y < LARGO - 1 && escenario[y + 1][x] != PARED) {
            y++;
            this.estado = y * ANCHO + x;
            return recompensas[y][x];
        } else {
            return PENALIZACION;
        }
    }

    private String accionStr(int accion) {
        switch (accion) {
            // izquierda
            case 0: {
                return "Izquierda";
            }
            // arriba
            case 1: {
                return "Arriba";
            }
            // derecha
            case 2: {
                return "Derecha";
            }
            // abajo
            case 3: {
                return "Abajo";
            }
            default: {
                return "No definido";
            }
        }
    }

    private boolean esFinal() {
        char casilla = escenario[y][x];
        return (casilla == RECOMPENSA || casilla == FANTASMA);
    }

    public static String fmt(float d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

}
