package es.isia.sm.model.celdas;

import java.awt.*;

public abstract class CeldaTransitable extends Celda {

    protected Direccion currentDirection;


    protected CeldaTransitable(Point position, Direccion direction) {
        super(position);
        currentDirection = direction;
    }

    @Override
    public boolean esTransitable() {
        return true;
    }

    public Direccion getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public String toString() {
        return String.valueOf(currentDirection.valor());
    }
}
