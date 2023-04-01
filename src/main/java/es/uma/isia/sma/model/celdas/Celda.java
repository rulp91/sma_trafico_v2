package es.uma.isia.sma.model.celdas;

import es.uma.isia.sma.model.coordenadas.Coordenada;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase abstracta que representa una celda en el entorno urbano.
 * <p>
 * Una celda tiene unas coordenadas únicas y puede ser transitable o no transitable.
 */
public abstract class Celda implements Serializable {
    private static final long serialVersionUID = 1572468285725371240L;
    private final Coordenada coordenadas;

    /**
     * Constructor
     *
     * @param coordenadas Coordenadas de la celda.
     */
    protected Celda(Coordenada coordenadas) {
        this.coordenadas = coordenadas;
    }

    /**
     * Devuelve las coordenadas de la celda.
     *
     * @return Coordenadas de la celda.
     */
    public Coordenada getCoordenadas() {
        return coordenadas;
    }

    /**
     * Indica si la celda es transitable o no.
     *
     * @return True si la celda es transitable, false en caso contrario.
     */
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
