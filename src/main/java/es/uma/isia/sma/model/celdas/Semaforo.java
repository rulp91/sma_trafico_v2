package es.uma.isia.sma.model.celdas;

import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

import java.io.Serializable;

public class Semaforo extends CeldaTransitable implements Serializable {

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
