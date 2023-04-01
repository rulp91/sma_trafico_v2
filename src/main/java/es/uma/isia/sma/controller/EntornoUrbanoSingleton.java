package es.uma.isia.sma.controller;

import es.uma.isia.sma.helper.GeneradorEntornoUrbano;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

public class EntornoUrbanoSingleton {
    private static EntornoUrbanoSingleton instance;
    private Celda[][] entornoUrbano;

    private EntornoUrbanoSingleton() {
        entornoUrbano = GeneradorEntornoUrbano.generarMockup();
    }

    public static synchronized EntornoUrbanoSingleton getInstance() {
        if (instance == null) {
            instance = new EntornoUrbanoSingleton();
        }
        return instance;
    }

    public Celda[][] getEntornoUrbano() {
        return entornoUrbano;
    }

    public CeldaTransitable getCeldaInicial() {
        int filas = entornoUrbano.length;

        // La celda inicial es la de la esquina inferior izquierda
        return (CeldaTransitable) entornoUrbano[filas - 1][0];
    }

    public CeldaTransitable getSiguienteCeldaTransitable(CeldaTransitable celdaActual) {

        if (celdaActual == null)
            return getCeldaInicial();

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
