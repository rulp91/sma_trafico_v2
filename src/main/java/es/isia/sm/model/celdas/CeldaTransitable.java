package es.isia.sm.model.celdas;

import java.awt.*;

public abstract class CeldaTransitable extends Celda {

    protected Direccion direccion;


    protected CeldaTransitable(Point position, Direccion direction) {
        super(position);
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
