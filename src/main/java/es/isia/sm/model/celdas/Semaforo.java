package es.isia.sm.model.celdas;

import es.isia.sm.model.coordenadas.Coordenada;
import es.isia.sm.model.coordenadas.Direccion;

public class Semaforo extends CeldaTransitable {

    public Semaforo(Coordenada coordenada, Direccion direction) {
        super(coordenada, direction);
    }

    public Semaforo(Coordenada position) {
        this(position, Direccion.generarDireccionAleatoria());
    }

    public void setAllowDirection(Direccion direction) {
        direccion = direction;
    }

    @Override
    public String toString() {
        return String.valueOf(Direccion.SEMAFORO);
    }
}
