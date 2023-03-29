package es.isia.sm.controller;

import es.isia.sm.controller.behaviour.ComportamientoCoche;
import es.isia.sm.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.Agent;

public class AgenteCoche  extends Agent {

    private CeldaTransitable celdaActual;
    private AID sceneAgentAID;
    @Override
    protected void setup() {

        // Se le pasa como parámetro la celda que ocupa el coche
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        System.out.println("My local name is " + getAID().getLocalName());
        sceneAgentAID = (AID) args[0];

       addBehaviour(new ComportamientoCoche(this));
    }

    public CeldaTransitable getCeldaActual() {
        return celdaActual;
    }

    public AID getSceneAgentAID() {
        return sceneAgentAID;
    }

    public void avance(CeldaTransitable celda) {
        celdaActual = celda;
        // System.out.println( getAID().getLocalName() + " avanza a " + currentCell.getMatrixPosition()+" en dirección " + currentCell);
    }
}
