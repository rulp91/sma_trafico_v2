package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteControlTrafico;
import es.uma.isia.sma.controller.EntornoUrbanoManager;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public class ComportamientoCentralitaRecepcionMensajesCoches extends SimpleBehaviour {
    private final AgenteControlTrafico agenteControlTrafico;
    private boolean done = false;

    public ComportamientoCentralitaRecepcionMensajesCoches(AgenteControlTrafico agenteControlTrafico) {
        this.agenteControlTrafico = agenteControlTrafico;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
        );
        ACLMessage msg = agenteControlTrafico.receive(mt);
        if (msg != null) {
            // Procesar el mensaje de AgenteCoche
            try {
                CeldaTransitable celdaActual = (CeldaTransitable) msg.getContentObject();
                CeldaTransitable siguienteCelda = EntornoUrbanoManager.getInstance().getSiguienteCeldaTransitable(celdaActual);
                if (siguienteCelda != null && !agenteControlTrafico.esPosicionOcupada(siguienteCelda.getCoordenadas())) {
                    // Si la siguiente celda es un semáforo, verifica si la dirección permitida es la actual
                    if (esSemaforoConDireccionPermitida(siguienteCelda, celdaActual)) {
                        System.out.println("Es un semaforo y la dirección está permitida");
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

            } catch (UnreadableException e) {
                // throw new RuntimeException(e);
            } catch (IOException e) {
                // throw new RuntimeException(e);
            }

            done = false;
        } else {
            block();
        }
    }

    private void enviarMensajeAvance(ACLMessage mensaje, CeldaTransitable siguienteCelda) throws IOException {
        ACLMessage respuesta = mensaje.createReply();
        respuesta.setPerformative(ACLMessage.AGREE);
        respuesta.setContent("Avanza a la celda " + siguienteCelda.getCoordenadas());
        respuesta.setContentObject(siguienteCelda);
        agenteControlTrafico.send(respuesta);
    }

    private void enviarMensajeRechazoAvance(ACLMessage mensaje) {
        ACLMessage reply = mensaje.createReply();
        reply.setPerformative(ACLMessage.REFUSE);
        reply.setContent("No puedes avanzar por ahora");

        agenteControlTrafico.send(reply);
    }


    private boolean esSemaforoConDireccionPermitida(CeldaTransitable siguienteCelda, CeldaTransitable celdaActual) {
        if (siguienteCelda instanceof Semaforo) {
            Semaforo semaforo = (Semaforo) siguienteCelda;
            return celdaActual != null && semaforo.getDireccion() == celdaActual.getDireccion();
        }
        return false;
    }

    private void liberarCeldaActual(Celda celdaActual) {
        if (celdaActual != null) {
            agenteControlTrafico.marcarPosicion(celdaActual.getCoordenadas(), false);
        }
    }

    private void ocuparSiguienteCelda(Celda siguienteCelda) {
        agenteControlTrafico.marcarPosicion(siguienteCelda.getCoordenadas(), true);
    }


    @Override
    public boolean done() {
        return done;
    }
}
