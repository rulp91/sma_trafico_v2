package es.uma.isia.sma.model.celdas;


import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

/**
 * Clase que representa un bloque en la simulación del tráfico.
 */
public class Bloque extends Celda {

    /**
     * Crea un nuevo objeto Bloque con la coordenada especificada.
     *
     * @param position la coordenada del bloque
     */
    public Bloque(Coordenada position) {
        super(position);
    }

    /**
     * Indica si la celda es transitable.
     *
     * @return false, ya que un bloque no es transitable.
     */
    @Override
    public boolean esTransitable() {
        return false;
    }

    /**
     * Devuelve una representación en forma de cadena de texto del objeto.
     *
     * @return la cadena  0
     */
    @Override
    public String toString() {
        return String.valueOf(Direccion.BLOQUE);
    }
}
