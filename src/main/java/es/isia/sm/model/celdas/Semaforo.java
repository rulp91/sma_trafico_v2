package es.isia.sm.model.celdas;
import java.awt.*;

public class Semaforo extends CeldaTransitable {

    public Semaforo(Point position, Direccion direction) {
        super(position, direction);
    }

    /**
     * Setea la dirección que permite el semaforo en cada momento
     *
     * @param direction
     */
    public void setAllowDirection(Direccion direction) {
        currentDirection = direction;
    }

    @Override
    public String toString() {
        return String.valueOf(Direccion.SEMAFORO);
    }
}
