package es.isia.sm.model.celdas;

import es.isia.sm.model.coordenadas.Coordenada;
import es.isia.sm.model.coordenadas.Direccion;

public class CeldaDireccionUnica extends CeldaTransitable {


    private static final long serialVersionUID = -6709606970479168453L;

    public CeldaDireccionUnica(Coordenada coordenada, Direccion direction) {
        super(coordenada, direction);
    }
}
