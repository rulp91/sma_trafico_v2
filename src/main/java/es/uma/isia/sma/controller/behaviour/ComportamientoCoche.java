package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteCoche;
import es.uma.isia.sma.controller.LoggerController;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase ComportamientoCoche extiende TickerBehaviour y define el comportamiento del agente Coche en el sistema.
 * El agente Coche envía periódicamente un mensaje al agente ControlTrafico solicitando la siguiente celda a la que
 * debe avanzar. Dependiendo de la respuesta recibida del agente ControlTrafico, el agente Coche avanzará a la siguiente
 * celda o permanecerá en su posición actual.
 * <p>
 * Este comportamiento se ejecuta periódicamente con un intervalo de tiempo predefinido de 1 segundo
 */
public class ComportamientoCoche extends TickerBehaviour {

    // Crear un Logger para la clase
    private static final Logger logger = Logger.getLogger(ComportamientoCoche.class.getName());
    static {
        LoggerController.getInstance().setupLogger(logger);
    }
    /**
     * El agente Coche al que pertenece este comportamiento.
     */
    private final AgenteCoche coche;

    /**
     * Crea una instancia de la clase ComportamientoCoche.
     *
     * @param a El agente Coche al que pertenece este comportamiento.
     */
    public ComportamientoCoche(AgenteCoche a) {
        super(a, 1000);
        coche = a;
    }

    @Override
    /**
     * Este método se ejecuta periódicamente según el período de tiempo establecido en la propiedad period.
     * En cada ejecución, el agente Coche envía un mensaje al agente Scene solicitando la siguiente celda a la
     * que debe avanzar. Si la respuesta del agente Scene es positiva, el agente Coche avanza a la siguiente celda,
     * si la respuesta es negativa, el agente Coche no avanza.
     */
    protected void onTick() {
        AID destinatario = coche.getAIDAgenteControlTrafico();
        if (destinatario != null) {
            solicitarSiguienteCeldaTransitable(destinatario);
            ACLMessage respuesta = coche.blockingReceive();
            if (respuesta != null) {
                procesarRespuestaControlTrafico(respuesta);
            } else {
                block();
            }
        }
    }

    /**
     * Envía una solicitud al agente ControlTrafico para obtener la siguiente CeldaTransitable en la que el agente Coche
     * puede avanzar. La solicitud se realiza utilizando un mensaje FIPA-REQUEST con el contenido "AvanceCoche" y el objeto
     * CeldaTransitable actual del agente Coche.
     *
     * @param destinatario El AID del agente ControlTrafico al que se enviará la solicitud.
     */
    private void solicitarSiguienteCeldaTransitable(AID destinatario) {
        ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
        mensaje.addReceiver(destinatario);
        try {
            mensaje.setContent("AvanceCoche");
            mensaje.setContentObject(coche.getCeldaActual());
            mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            mensaje.setLanguage("Java");
            mensaje.setOntology("Ontologia de Coches");
            coche.send(mensaje);
        } catch (IOException e) {
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
        } catch (UnreadableException e) {
            logger.log(Level.WARNING, "Error al leer el contenido del mensaje de respuesta", e);
        }
    }

}
