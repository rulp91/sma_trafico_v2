package es.isia.sm.model.celdas;


import java.awt.*;

public class Bloque extends Celda {

    public Bloque(Point position) {
        super(position);
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    @Override
    public String toString() {
        return "0";
    }
}
