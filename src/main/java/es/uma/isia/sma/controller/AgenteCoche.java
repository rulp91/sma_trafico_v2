package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoCoche;
import es.uma.isia.sma.controller.behaviour.ComportamientoCocheAvanceBajoDemanda;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import jade.core.behaviours.ParallelBehaviour;

import static es.uma.isia.sma.controller.IDescripcionServicios.COCHES_SERVICE_DESCRIPTION;


/**
 * Clase AgenteCoche que representa un agente coche en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del coche.
 */
public class AgenteCoche extends AgenteElementoTrafico {

    private CeldaTransitable celdaActual;

    @Override
    /**
     * Método setup que se ejecuta al iniciar el agente.
     * Recibe como argumentos el AID del agente escena al que debe enviar mensajes.
     */
    protected void setup() {

        logger.info("Creación de agente:  " + getAID().getLocalName());

        //Registro el agente en el DF
        registrarAgente(COCHES_SERVICE_DESCRIPTION);

        // Añadir comportamiento
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();

        // Añadir comportamientos a ParallelBehaviour
        parallelBehaviour.addSubBehaviour(new ComportamientoCoche(this));
        parallelBehaviour.addSubBehaviour(new ComportamientoCocheAvanceBajoDemanda(this));

        // Añadir ParallelBehaviour al agente
        addBehaviour(parallelBehaviour);
    }

    /**
     * Método que devuelve la celda transitable actual en la que se encuentra el coche.
     *
     * @return La celda transitable actual en la que se encuentra el coche.
     */
    public CeldaTransitable getCeldaActual() {
        return celdaActual;
    }

    /**
     * Método que actualiza la celda transitable actual en la que se encuentra el coche.
     *
     * @param celda La nueva celda transitable actual en la que se encuentra el coche.
     */
    public void avance(CeldaTransitable celda) {
        celdaActual = celda;
    }

}
