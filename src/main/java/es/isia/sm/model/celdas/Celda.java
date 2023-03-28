package es.isia.sm.model.celdas;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public abstract class Celda implements Serializable {
    private final Point position;

    public Celda(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public abstract boolean esTransitable();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Celda celda = (Celda) o;
        return Objects.equals(position, celda.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
