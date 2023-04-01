package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteControlTrafico;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ComportamientoCentralitaRecepcionMensajesSemaforos extends SimpleBehaviour {
    private final AgenteControlTrafico agenteControlTrafico;
    private boolean done = false;

    public ComportamientoCentralitaRecepcionMensajesSemaforos(AgenteControlTrafico agenteControlTrafico) {
        this.agenteControlTrafico = agenteControlTrafico;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchContent("CambioDireccionPermitidaSemaforo")
        );
        ACLMessage msg = agenteControlTrafico.receive(mt);
        if (msg != null) {
            // Procesar el mensaje de AgenteSemaforo
            try {
                Semaforo semaforo = (Semaforo) msg.getContentObject();
                agenteControlTrafico.cambioDireccionPermitidaSemaforo(semaforo);
            } catch (UnreadableException e) {
                System.err.println(e.getMessage());
            }
            done = false;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }
}
