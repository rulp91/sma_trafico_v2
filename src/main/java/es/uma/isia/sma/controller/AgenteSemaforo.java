package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSemaforo;
import es.uma.isia.sma.controller.behaviour.ComportamientoSemaforoManejarSuscripciones;
import es.uma.isia.sma.model.celdas.Semaforo;
import es.uma.isia.sma.model.coordenadas.Direccion;
import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;

import static es.uma.isia.sma.controller.IDescripcionServicios.SEMAFOROS_SERVICE_DESCRIPTION;

/**
 * Clase AgenteSemaforo que representa un agente semáforo en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del semáforo.
 */
public class AgenteSemaforo extends AgenteElementoTrafico {


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

        // Crear un ParallelBehaviour
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);

        // Añadir comportamientos al ParallelBehaviour
        parallelBehaviour.addSubBehaviour(new ComportamientoSemaforo(this));
        parallelBehaviour.addSubBehaviour(new ComportamientoSemaforoManejarSuscripciones(this));

        // Añadir el ParallelBehaviour al agente
        addBehaviour(parallelBehaviour);
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

    /**
     * Establece el AID del agente de control de tráfico asociado a este agente.
     *
     * @param aid El AID del agente de control de tráfico.
     */
    public void setAgenteControlTraficoAID(AID aid) {
        agenteControlTraficoAID = aid;
    }
}
