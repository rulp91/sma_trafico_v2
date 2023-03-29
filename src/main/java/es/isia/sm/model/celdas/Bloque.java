package es.isia.sm.model.celdas;


import es.isia.sm.model.coordenadas.Coordenada;
import es.isia.sm.model.coordenadas.Direccion;


public class Bloque extends Celda {

    public Bloque(Coordenada position) {
        super(position);
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(Direccion.BLOQUE);
    }
}
