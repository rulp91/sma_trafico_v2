package es.isia.sm.model.celdas;

import es.isia.sm.model.coordenadas.Coordenada;
import es.isia.sm.model.coordenadas.Direccion;

public class CeldaDireccionUnica extends CeldaTransitable {


    public CeldaDireccionUnica(Coordenada coordenada, Direccion direction) {
        super(coordenada, direction);
    }
}
