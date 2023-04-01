package es.uma.isia.sma.model.celdas;

import es.uma.isia.sma.model.coordenadas.Coordenada;
import es.uma.isia.sma.model.coordenadas.Direccion;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase abstracta que representa una celda transitable en el modelo del entorno simulado.
 * <p>
 * Extiende la clase {@link Celda} y añade una dirección asociada a la celda.
 */
public abstract class CeldaTransitable extends Celda implements Serializable {

    private static final long serialVersionUID = 1916811518241750607L;
    protected Direccion direccion;

    /**
     * Constructor
     *
     * @param coordenada La coordenada de la celda transitable.
     * @param direccion  La dirección asociada a la celda transitable.
     */
    protected CeldaTransitable(Coordenada coordenada, Direccion direccion) {
        super(coordenada);
        this.direccion = direccion;
    }

    @Override
    public boolean esTransitable() {
        return true;
    }

    /**
     * Devuelve la dirección asociada a la celda transitable.
     *
     * @return La dirección asociada a la celda transitable.
     */
    public Direccion getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return String.valueOf(direccion.valor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CeldaTransitable that = (CeldaTransitable) o;
        return direccion == that.direccion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direccion);
    }
}
