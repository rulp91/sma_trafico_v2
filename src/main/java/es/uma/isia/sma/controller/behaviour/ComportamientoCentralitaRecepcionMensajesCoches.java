package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteControlTrafico;
import es.uma.isia.sma.controller.EntornoUrbanoSingleton;
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
                CeldaTransitable siguienteCelda = EntornoUrbanoSingleton.getInstance().getSiguienteCeldaTransitable(celdaActual);
                if(siguienteCelda!=null && !agenteControlTrafico.estaOcupada(siguienteCelda.getCoordenadas())){

                    //Si la siguiente celda es un semaforo hay que saber si la dirección permitida es en la que voy
                    // if(siguienteCelda instanceof Semaforo)


                    //Si la celda actual no es null, liberala
                    if(celdaActual!=null)
                        agenteControlTrafico.marcarPosicion(celdaActual.getCoordenadas(), false);

                    // Marca la actual como ocupada
                    agenteControlTrafico.marcarPosicion(siguienteCelda.getCoordenadas(), true);
                    enviarMensajeAvance(msg, siguienteCelda);

                }else{
                    enviarMensajeRechazoAvance(msg);
                }

            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            done = false;
        } else {
            block();
        }
    }
    private void enviarMensajeAvance(ACLMessage msg, CeldaTransitable siguienteCelda) throws IOException {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.AGREE);
        reply.setContent("Avanza a la celda "+ siguienteCelda.getCoordenadas());
        reply.setContentObject(siguienteCelda);
        myAgent.send(reply);
    }

    private void enviarMensajeRechazoAvance(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.AGREE);
        reply.setContent("No puedes avanzar por ahora");

        myAgent.send(reply);
    }



    @Override
    public boolean done() {
        return done;
    }
}
