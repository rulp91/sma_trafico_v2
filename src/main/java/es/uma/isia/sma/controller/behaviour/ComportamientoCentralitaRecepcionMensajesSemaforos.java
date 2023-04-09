package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteControlTrafico;
import es.uma.isia.sma.controller.LoggerController;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comportamiento que se encarga de recibir los mensajes enviados por los agentes semáforo
 * y actualizar la dirección permitida en el agente control de tráfico.
 */
public class ComportamientoCentralitaRecepcionMensajesSemaforos extends SimpleBehaviour {
    private static final Logger logger = LoggerController.getInstance().getLogger();

    private final AgenteControlTrafico agenteControlTrafico;
    private boolean done = false;

    /**
     * Crea un nuevo comportamiento de recepción de mensajes de semáforos.
     *
     * @param agenteControlTrafico agente control de tráfico al que pertenece este comportamiento.
     */
    public ComportamientoCentralitaRecepcionMensajesSemaforos(AgenteControlTrafico agenteControlTrafico) {
        this.agenteControlTrafico = agenteControlTrafico;
    }

    /**
     * Recibe los mensajes enviados por los agentes semáforo y actualiza la dirección permitida
     * en el agente control de tráfico.
     */
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
            try {
                Semaforo semaforo = (Semaforo) msg.getContentObject();
                agenteControlTrafico.actualizarDireccionPermitidaSemaforo(semaforo);
            } catch (UnreadableException e) {
                logger.log(Level.WARNING, "Error al obtener el contenido del semaforo ", e);
            }
        } else {
            block();
        }
    }

    /**
     * Indica si el comportamiento ha finalizado su ejecución.
     * @return true si el comportamiento ha finalizado, false en caso contrario.
     */
    @Override
    public boolean done() {
        return done;
    }
}
