package es.isia.sm.model.celdas;

import es.isia.sm.model.coordenadas.Coordenada;
import es.isia.sm.model.coordenadas.Direccion;


public abstract class CeldaTransitable extends Celda {

    protected Direccion direccion;

    protected CeldaTransitable(Coordenada coordenada, Direccion direction) {
        super(coordenada);
        direccion = direction;
    }

    @Override
    public boolean esTransitable() {
        return true;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return String.valueOf(direccion.valor());
    }
}
