package es.isia.sm.controller;

import es.isia.sm.controller.behaviour.ComportamientoCoche;
import es.isia.sm.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.Agent;

/**
 * Clase AgenteCoche que representa un agente coche en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del coche.
 */
public class AgenteCoche  extends Agent {

    private CeldaTransitable celdaActual;
    private AID sceneAgentAID;

    @Override
    /**
     * Método setup que se ejecuta al iniciar el agente.
     * Recibe como argumentos el AID del agente escena al que debe enviar mensajes.
     */
    protected void setup() {

        // Se le pasa como parámetro la celda que ocupa el coche
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        System.out.println("My local name is " + getAID().getLocalName());
        sceneAgentAID = (AID) args[0];

       addBehaviour(new ComportamientoCoche(this));
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
     * Retorna el AID del control de tráfico
     *
     * @return AID del agente que contreola el tráfico
     */
    public AID getSceneAgentAID() {
        return sceneAgentAID;
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
