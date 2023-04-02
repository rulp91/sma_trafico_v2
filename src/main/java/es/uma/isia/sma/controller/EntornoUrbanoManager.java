package es.uma.isia.sma.controller;

import es.uma.isia.sma.helper.GeneradorEntornoUrbano;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import es.uma.isia.sma.model.celdas.Semaforo;
import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase proporciona acceso y manejo del entorno urbano, incluyendo la obtención de celdas específicas, semáforos y
 * celdas transitables. El entorno urbano se representa como una matriz bidimensional de celdas, que pueden ser
 * transitables o no transitables.
 */
public class EntornoUrbanoManager {
    private static EntornoUrbanoManager instance;
    private Celda[][] entornoUrbano;

    private GeneradorEntornoUrbano helperEntornoUrbano;

    private EntornoUrbanoManager() {

    }

    public static synchronized EntornoUrbanoManager getInstance() {
        if (instance == null) {
            instance = new EntornoUrbanoManager();
        }
        return instance;
    }


    public Celda[][] getEntornoUrbano() {
        return entornoUrbano;
    }


    /**
     * Genera un entorno urbano con el número de filas y columnas especificado.
     *
     * @param filas Número de filas del entorno urbano.
     * @param columnas Número de columnas del entorno urbano.
     */
    public void generaEntornoUrbano(int filas, int columnas) {
        helperEntornoUrbano = new GeneradorEntornoUrbano(filas, columnas);
    }

    /**
     * Genera casillas no transitables en el entorno urbano.
     *
     * @param numeroCasillasNoTransitables Número de casillas no transitables a generar.
     */
    public void generarCasillasNoTransitables(int numeroCasillasNoTransitables) {
        helperEntornoUrbano.generarCasillasNoTransitables(numeroCasillasNoTransitables);
    }

    /**
     * Genera casillas transitables en el entorno urbano y coloca semáforos.
     */
    public void generarCasillasTransitables() {
        helperEntornoUrbano.asignarDirecciones();
        helperEntornoUrbano.reparaColisionesHorizontales();
        helperEntornoUrbano.reparaColisionesVerticales();
        helperEntornoUrbano.colocarSemaforosHorizontal();
        helperEntornoUrbano.colocarSemaforosVertical();
        System.out.println(helperEntornoUrbano);
        entornoUrbano = helperEntornoUrbano.generarEntornoUrbano();
    }

    /**
     * Devuelve la entrada del entorno urbano, que es la celda inicial desde donde los coches pueden comenzar a moverse.
     *
     * @return la entrada del entorno urbano, que es una instancia de {@link CeldaTransitable}
     */
    public CeldaTransitable getEntradaEntornoUrbano() {
        int filas = entornoUrbano.length;

        // La celda inicial es la de la esquina inferior izquierda
        return (CeldaTransitable) entornoUrbano[filas - 1][0];
    }

    /**
     * Obtiene una lista de todos los semáforos presentes en el entorno urbano.
     *
     * @return una lista de {@link Semaforo} que contiene todos los semáforos en el entorno urbano
     */
    public List<Semaforo> getSemaforos() {
        List<Semaforo> celdasSemaforo = new ArrayList<>();

        for (int i = 0; i < entornoUrbano.length; i++) {
            for (int j = 0; j < entornoUrbano[i].length; j++) {
                Celda celda = entornoUrbano[i][j];
                if (celda instanceof Semaforo) {
                    celdasSemaforo.add((Semaforo) celda);
                }
            }
        }

        return celdasSemaforo;
    }

    /**
     * Dada la celda transitable actual, obtiene la siguiente celda transitable en la dirección de la celda actual.
     * Si la celda actual es null significa que el coche aún no ha entrado al entorno urbano,
     * asi que devuelve la entrada del entorno urbano.
     *
     * @param celdaActual la celda transitable actual desde la que se busca la siguiente celda transitable
     * @return la siguiente {@link CeldaTransitable} en la dirección de la celda actual, o null si no hay una siguiente celda transitable
     */
    public CeldaTransitable getSiguienteCeldaTransitable(CeldaTransitable celdaActual) {

        if (celdaActual == null)
            return getEntradaEntornoUrbano();

        Coordenada coordenadaActual = celdaActual.getCoordenadas();
        Direccion direccion = celdaActual.getDireccion();

        int filaActual = coordenadaActual.x;
        int columnaActual = coordenadaActual.y;

        int siguienteFila = filaActual;
        int siguienteColumna = columnaActual;

        // Calcular las coordenadas de la siguiente celda basándose en la dirección
        switch (direccion) {
            case NORTE:
                siguienteFila--;
                break;
            case ESTE:
                siguienteColumna++;
                break;
            case SUR:
                siguienteFila++;
                break;
            case OESTE:
                siguienteColumna--;
                break;
        }

        // Verificar si las coordenadas de la siguiente celda están dentro de los límites de la matriz entornoUrbano
        if (siguienteFila >= 0 && siguienteFila < entornoUrbano.length &&
                siguienteColumna >= 0 && siguienteColumna < entornoUrbano[0].length) {

            Celda siguienteCelda = entornoUrbano[siguienteFila][siguienteColumna];

            // Verificar si la siguiente celda es transitable
            if (siguienteCelda instanceof CeldaTransitable) {
                return (CeldaTransitable) siguienteCelda;
            }
        }

        return null;
    }

}
