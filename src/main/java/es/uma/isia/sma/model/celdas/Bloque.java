package es.uma.isia.sma.model.celdas;


import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;


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
