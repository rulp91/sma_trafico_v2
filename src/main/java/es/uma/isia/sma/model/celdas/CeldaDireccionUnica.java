package es.uma.isia.sma.model.celdas;

import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

public class CeldaDireccionUnica extends CeldaTransitable {


    private static final long serialVersionUID = -6709606970479168453L;

    public CeldaDireccionUnica(Coordenada coordenada, Direccion direction) {
        super(coordenada, direction);
    }
}
