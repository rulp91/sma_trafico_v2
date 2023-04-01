package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteControlTrafico;
import es.uma.isia.sma.controller.EntornoUrbanoManager;
import es.uma.isia.sma.controller.LoggerController;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Comportamiento que se ejecuta en el agente de control de tráfico para recibir y procesar
 * los mensajes de los vehículos que solicitan avanzar en el entorno de simulación.
 */
public class ComportamientoCentralitaRecepcionMensajesCoches extends SimpleBehaviour {
    private static final Logger logger = LoggerController.getInstance().getLogger();

    private final AgenteControlTrafico agenteControlTrafico;
    private boolean done = false;

    /**
     * Crea un nuevo comportamiento de recepción de mensajes de vehículos.
     *
     * @param agenteControlTrafico El agente de control del tráfico que ejecutará este comportamiento.
     */
    public ComportamientoCentralitaRecepcionMensajesCoches(AgenteControlTrafico agenteControlTrafico) {
        this.agenteControlTrafico = agenteControlTrafico;
    }

    /**
     * Espera a recibir un mensaje de un vehículo y luego lo procesa para determinar si puede avanzar en el entorno
     * de simulación.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
        );
        ACLMessage msg = agenteControlTrafico.receive(mt);
        if (msg != null) {
            procesarMensajeVehiculo(msg);
            done = false;
        } else {
            block();
        }
    }

    /**
     * Procesa el mensaje recibido de un vehículo para determinar si puede avanzar en el entorno de simulación.
     *
     * @param msg El mensaje recibido de un vehículo.
     */
    private void procesarMensajeVehiculo(ACLMessage msg) {
        try {
            CeldaTransitable celdaActual = (CeldaTransitable) msg.getContentObject();
            CeldaTransitable siguienteCelda = EntornoUrbanoManager.getInstance().getSiguienteCeldaTransitable(celdaActual);
            if (siguienteCelda != null && !agenteControlTrafico.esPosicionOcupada(siguienteCelda.getCoordenadas())) {
                // Si la siguiente celda es un semáforo, verifica si la dirección permitida es la actual
                if (esSemaforoConDireccionPermitida(siguienteCelda, celdaActual)) {
                    logger.info("Es un semaforo y la dirección está permitida");
                    liberarCeldaActual(celdaActual);
                    ocuparSiguienteCelda(siguienteCelda);
                    enviarMensajeAvance(msg, siguienteCelda);
                } else if (!(siguienteCelda instanceof Semaforo)) {
                    liberarCeldaActual(celdaActual);
                    ocuparSiguienteCelda(siguienteCelda);
                    enviarMensajeAvance(msg, siguienteCelda);
                } else {
                    enviarMensajeRechazoAvance(msg);
                }
            } else {
                enviarMensajeRechazoAvance(msg);
            }

        } catch (UnreadableException | IOException e) {
            logger.log(Level.WARNING, "Error al procesar mensaje vehiculo ", e);
        }
    }

    /**
     * Envia un mensaje de acuerdo al avance del vehículo en el mapa.
     *
     * @param mensaje        el mensaje original enviado por el vehículo.
     * @param siguienteCelda la celda a la que se va a avanzar.
     * @throws IOException si ocurre un error al intentar enviar el mensaje.
     */
    private void enviarMensajeAvance(ACLMessage mensaje, CeldaTransitable siguienteCelda) throws IOException {
        ACLMessage respuesta = mensaje.createReply();
        respuesta.setPerformative(ACLMessage.AGREE);
        respuesta.setContent("Avanza a la celda " + siguienteCelda.getCoordenadas());
        respuesta.setContentObject(siguienteCelda);
        agenteControlTrafico.send(respuesta);
    }

    /**
     * Envia un mensaje de rechazo cuando el vehículo no puede avanzar.
     *
     * @param mensaje el mensaje original enviado por el vehículo.
     */
    private void enviarMensajeRechazoAvance(ACLMessage mensaje) {
        ACLMessage reply = mensaje.createReply();
        reply.setPerformative(ACLMessage.REFUSE);
        reply.setContent("No puedes avanzar por ahora");

        agenteControlTrafico.send(reply);
    }


    /**
     * Verifica si la siguiente celda es un semáforo y si la dirección de movimiento está permitida.
     *
     * @param siguienteCelda la celda a la que se va a avanzar.
     * @param celdaActual    la celda en la que se encuentra actualmente el vehículo.
     * @return true si el semáforo permite avanzar en la dirección actual, false en caso contrario.
     */
    private boolean esSemaforoConDireccionPermitida(CeldaTransitable siguienteCelda, CeldaTransitable celdaActual) {
        if (siguienteCelda instanceof Semaforo) {
            Semaforo semaforo = (Semaforo) siguienteCelda;
            return celdaActual != null && semaforo.getDireccion() == celdaActual.getDireccion();
        }
        return false;
    }

    /**
     * Libera la celda actual en la que se encuentra el vehículo, la marca en la matriz como libre o transitable
     *
     * @param celdaActual la celda en la que se encuentra actualmente el vehículo.
     */
    private void liberarCeldaActual(Celda celdaActual) {
        if (celdaActual != null)
            agenteControlTrafico.marcarPosicion(celdaActual.getCoordenadas(), false);
    }

    /**
     * Marca como ocupada la siguiente celda a la que se va a avanzar.
     *
     * @param siguienteCelda la celda a la que se va a avanzar.
     */
    private void ocuparSiguienteCelda(Celda siguienteCelda) {
        agenteControlTrafico.marcarPosicion(siguienteCelda.getCoordenadas(), true);
    }

    @Override
    public boolean done() {
        return done;
    }
}
