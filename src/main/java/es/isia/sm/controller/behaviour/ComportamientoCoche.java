package es.isia.sm.controller.behaviour;

import es.isia.sm.controller.AgenteCoche;
import es.isia.sm.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public class ComportamientoCoche extends TickerBehaviour {

    private static final int period = 1000;

    private final AgenteCoche coche;

    public ComportamientoCoche(AgenteCoche a) {
        super(a, period);
        coche = a;
    }

    @Override
    protected void onTick() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

        // Establecer destinatario del mensaje (Agente Scene)
        AID destinatario = new AID(coche.getSceneAgentAID().getLocalName(), AID.ISLOCALNAME);
        msg.addReceiver(destinatario);

        // Establecer contenido del mensaje
        try {

            msg.setContent("AvanceCoche");
            msg.setContentObject(coche.getCeldaActual());
            // Establecer el protocolo FIPA-Request
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

            // Establecer la codificación del mensaje
            msg.setLanguage("Java");
            msg.setOntology("Ontologia de Coches");

            // Enviar mensaje
            coche.send(msg);

            // Esperar respuesta
            ACLMessage respuesta = coche.blockingReceive();
            if (respuesta != null) {
                if (respuesta.getPerformative() == ACLMessage.AGREE) {
                    System.out.println("El agente Scene ha aceptado la petición.");
                    CeldaTransitable siguienteCelda = (CeldaTransitable) respuesta.getContentObject();
                    coche.avance(siguienteCelda);
                } else if (respuesta.getPerformative() == ACLMessage.REFUSE) {
                    //System.out.println("El agente Scene ha rechazado la petición.");
                } else {
                    //System.out.println("El agente Scene ha respondido con un mensaje no esperado.");
                }
            } else {
                block();
            }
        } catch (IOException e) {
            System.err.println(getClass() + " " +e.getMessage());
        } catch (UnreadableException e) {
            System.err.println(getClass() + " " +e.getMessage());
        }
    }
}
