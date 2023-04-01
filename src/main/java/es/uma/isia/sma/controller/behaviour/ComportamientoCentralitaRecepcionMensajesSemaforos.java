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
        // Como enfoque alternativo a MessageTemplate.MatchContent, envío el objeto Semáforo directamente como contenido
        // del mensaje y luego filtro los mensajes recibidos utilizando una condición personalizada en el comportamiento
        // receptor. Esto se debe a que no me está funcionando MatchContent
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                new MessageTemplate((ACLMessage msg) -> {
                    try {
                        return msg.getContentObject() instanceof Semaforo;
                    } catch (UnreadableException e) {
                        return false;
                    }
                })
        );
        ACLMessage msg = agenteControlTrafico.receive(mt);
        if (msg != null) {
            // Procesar el mensaje de AgenteSemaforo
            try {
                Semaforo semaforo = (Semaforo) msg.getContentObject();
                agenteControlTrafico.actualizarDireccionPermitidaSemaforo(semaforo);
            } catch (UnreadableException e) {
             //   System.err.println(e.getMessage());
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }
}
