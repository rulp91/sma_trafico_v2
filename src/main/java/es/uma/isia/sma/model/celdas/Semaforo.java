package es.uma.isia.sma.model.celdas;

import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

import java.io.Serializable;

/**
 * Clase que representa un semáforo en el modelo de simulación de tráfico.
 *
 * Extiende la clase {@link CeldaTransitable}.
 * Un semáforo es una celda transitable que solo permite el paso en la dirección permitida marcada como verde, el
 * resto de direcciones están en rojo, representa un semaforo como los semaforos americanos colgados
 * en los cruces de cuatro direcciones.
 */
public class Semaforo extends CeldaTransitable implements Serializable {

    private static final long serialVersionUID = 6500957949937663625L;

    /**
     * Crea un objeto Semaforo en una posición dada.
     * La dirección permitida es aleatoria inicialmente.
     *
     * @param position coordenada en la que se encuentra el semáforo.
     */
    public Semaforo(Coordenada position) {
        super(position, Direccion.generarDireccionAleatoria());
    }

    /**
     * Establece la dirección de paso permitida en el semáforo.
     *
     * @param direction dirección permitida en el semáforo.
     */
    public void direccionPermitida(Direccion direction) {
        direccion = direction;
    }

    @Override
    public String toString() {
        return String.valueOf(Direccion.SEMAFORO);
    }
}
