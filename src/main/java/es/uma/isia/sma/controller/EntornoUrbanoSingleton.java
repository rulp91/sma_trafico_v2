package es.uma.isia.sma.controller;

import es.uma.isia.sma.helper.GeneradorEntornoUrbano;
import es.uma.isia.sma.model.celdas.Celda;

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
}
