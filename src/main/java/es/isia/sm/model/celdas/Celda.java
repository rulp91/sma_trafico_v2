package es.isia.sm.model.celdas;

import es.isia.sm.model.coordenadas.Coordenada;

import java.io.Serializable;
import java.util.Objects;

public abstract class Celda implements Serializable {
    private final Coordenada coordenadas;

    public Celda(Coordenada coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Coordenada getCoordenadas() {
        return coordenadas;
    }

    public abstract boolean esTransitable();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Celda celda = (Celda) o;
        return Objects.equals(coordenadas, celda.coordenadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordenadas);
    }
}
