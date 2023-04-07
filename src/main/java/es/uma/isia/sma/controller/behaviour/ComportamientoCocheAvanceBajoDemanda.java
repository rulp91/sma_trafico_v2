package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteCoche;
import es.uma.isia.sma.controller.LoggerController;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static es.uma.isia.sma.controller.IACLTiposMensaje.ACL_MENSAJE_TIPO_AVANCE_COCHE;
import static es.uma.isia.sma.controller.IACLTiposMensaje.ACL_MENSAJE_TIPO_AVANCE_COCHE_ACTUALIZACION_SEMAFORO;

/**
 * La clase ComportamientoCocheAvanceBajoDemanda extiende CyclicBehaviour y define el comportamiento
 * del agente Coche en el sistema cuando debe avanzar bajo demanda. El agente Coche espera recibir
 * un mensaje REQUEST con el contenido "AvanceCocheCambioSemaforo" para solicitar un avance en el
 * entorno de tráfico. Tras recibir el mensaje, el agente Coche realiza una petición de avance al
 * agente ControlTrafico.
 * <p>
 * Este comportamiento se ejecuta de forma cíclica y sólo actúa cuando recibe el mensaje apropiado.
 */
public class ComportamientoCocheAvanceBajoDemanda extends CyclicBehaviour {

    private static final Logger logger = LoggerController.getInstance().getLogger();

    /**
     * El agente Coche al que pertenece este comportamiento.
     */
    private final AgenteCoche coche;

    public ComportamientoCocheAvanceBajoDemanda(AgenteCoche agenteCoche) {
        coche = agenteCoche;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null && msg.getPerformative() == ACLMessage.REQUEST && ACL_MENSAJE_TIPO_AVANCE_COCHE_ACTUALIZACION_SEMAFORO.equals(msg.getContent())) {
            realizarPeticionAvance();
            ACLMessage respuesta = coche.blockingReceive();
            if (respuesta != null) {
                procesarRespuestaControlTrafico(respuesta);
            }
        } else {
            block();
        }
    }

    /**
     * Realiza una petición de avance al agente ControlTrafico para que el agente Coche pueda avanzar
     * en el entorno de tráfico. La petición se realiza utilizando un mensaje FIPA-REQUEST con el
     * contenido "AvanceCoche" y el objeto CeldaTransitable actual del agente Coche. El mensaje
     * incluye también el protocolo, el lenguaje y la ontología utilizados en la comunicación.
     * En caso de error al enviar el mensaje, se registra una advertencia en el logger.
     */
    private void realizarPeticionAvance() {
        ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
        mensaje.addReceiver(coche.getAIDAgenteControlTrafico());
        try {
            mensaje.setContent(ACL_MENSAJE_TIPO_AVANCE_COCHE);
            mensaje.setContentObject(coche.getCeldaActual());
            mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            mensaje.setLanguage("Java");
            mensaje.setOntology("Ontologia de Coches");
            coche.send(mensaje);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al enviar el mensaje de solicitud de siguiente celda", e);
        }
    }

    /**
     * Procesa la respuesta recibida del agente ControlTrafico. Si la respuesta es de tipo AGREE, el agente Coche avanza
     * a la siguiente CeldaTransitable proporcionada en el mensaje. Si la respuesta es de otro tipo, no se realiza ninguna acción.
     *
     * @param respuestaControlTrafico El mensaje ACLMessage que contiene la respuesta del agente ControlTrafico.
     */
    private void procesarRespuestaControlTrafico(ACLMessage respuestaControlTrafico) {
        try {
            if (respuestaControlTrafico.getPerformative() == ACLMessage.AGREE) {
                CeldaTransitable siguienteCelda = (CeldaTransitable) respuestaControlTrafico.getContentObject();
                coche.avance(siguienteCelda);
                logger.log(Level.INFO, "El coche " + coche.getAID().getLocalName() + " avanza a " + siguienteCelda.getCoordenadas());
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al leer el contenido del mensaje de respuesta", e);
        }
    }

}
