package es.isia.sm.model.celdas;

import es.isia.sm.model.coordenadas.Coordenada;
import es.isia.sm.model.coordenadas.Direccion;

public class Semaforo extends CeldaTransitable {

    private static final long serialVersionUID = 6500957949937663625L;

    public Semaforo(Coordenada position) {
        super(position, Direccion.generarDireccionAleatoria());
    }

    public void direccionPermitida(Direccion direction) {
        direccion = direction;
    }

    @Override
    public String toString() {
        return String.valueOf(Direccion.SEMAFORO);
    }
}
