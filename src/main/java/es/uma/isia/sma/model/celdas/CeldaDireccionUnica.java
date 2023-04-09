package es.uma.isia.sma.model.celdas;

import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

/**
 * La clase CeldaDireccionUnica representa una casilla transitable con una única dirección en la que se pueden
 * mover los coches siempre que no esté ocupada. Esta clase hereda de CeldaTransitable y su constructor permite
 * establecer la coordenada y la dirección única de la celda.
 */
public class CeldaDireccionUnica extends CeldaTransitable {


    private static final long serialVersionUID = -6709606970479168453L;


    /**
     * Construye una nueva celda transitable con una dirección única.
     *
     * @param coordenada La coordenada de la celda.
     * @param direction  La dirección única en la que se puede transitar.
     */
    public CeldaDireccionUnica(Coordenada coordenada, Direccion direction) {
        super(coordenada, direction);
    }
}
