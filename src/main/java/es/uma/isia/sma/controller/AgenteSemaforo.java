package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSemaforo;
import es.uma.isia.sma.model.celdas.Semaforo;
import es.uma.isia.sma.model.coordenadas.Direccion;
import jade.core.AID;
import jade.core.Agent;

/**
 * Clase AgenteSemaforo que representa un agente semáforo en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del semáforo.
 */
public class AgenteSemaforo extends Agent {
    private Semaforo semaforo;
    private AID sceneAgentAID;

    @Override
    protected void setup() {
        // Se le pasa como parámetro la celda que ocupa el semaforo
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        System.out.println("My local name is " + getAID().getLocalName());
        semaforo = (Semaforo) args[0];
        sceneAgentAID = (AID) args[1];
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
     * Método que devuelve el AID del agente escena al que debe enviar mensajes.
     *
     * @return El AID del agente escena al que debe enviar mensajes.
     */
    public void cambiaDireccionPermitida() {
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
     * Retorna el AID del control de tráfico
     *
     * @return AID del agente que contreola el tráfico
     */
    public AID getSceneAgentAID() {
        return sceneAgentAID;
    }
}
