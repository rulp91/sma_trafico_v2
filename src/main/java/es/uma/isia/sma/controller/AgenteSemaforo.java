package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSemaforo;
import es.uma.isia.sma.model.celdas.Semaforo;
import es.uma.isia.sma.model.coordenadas.Direccion;

/**
 * Clase AgenteSemaforo que representa un agente semáforo en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del semáforo.
 */
public class AgenteSemaforo extends AgenteElementoTrafico {

    private static final String SEMAFOROS_SERVICE_DESCRIPTION = "semaforos";
    private Semaforo semaforo;

    @Override
    protected void setup() {

        // Se le pasa como parámetro la celda que ocupa el semaforo
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        logger.info("Creación de agente:  " + getAID().getLocalName());
        semaforo = (Semaforo) args[0];

        //Registro el agente en el DF
        registrarAgente(SEMAFOROS_SERVICE_DESCRIPTION);

        // Añadir comportamiento
        addBehaviour(new ComportamientoSemaforo(this));
    }

    /**
     * Método que devuelve el objeto Semaforo asociado al agente.
     *
     * @return El objeto Semaforo asociado al agente.
     */
    public Semaforo getSemaforo() {
        return semaforo;
    }

    /**
     * Cambia la dirección permitida del semáforo.
     */
    public void cambiarDireccionPermitida() {
        switch (semaforo.getDireccion()) {
            case NORTE:
                semaforo.direccionPermitida(Direccion.ESTE);
                break;
            case ESTE:
                semaforo.direccionPermitida(Direccion.SUR);
                break;
            case SUR:
                semaforo.direccionPermitida(Direccion.OESTE);
                break;
            case OESTE:
                semaforo.direccionPermitida(Direccion.NORTE);
                break;
        }
    }

}
