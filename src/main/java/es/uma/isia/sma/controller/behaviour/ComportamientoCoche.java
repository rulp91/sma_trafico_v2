package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteCoche;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

/**
 * Esta clase define el comportamiento del agente Coche. El agente Coche es un agente que envía un mensaje
 * a un agente Scene solicitando la siguiente celda a la que debe avanzar y espera una respuesta. En función
 * de la respuesta recibida, el agente Coche avanza a la siguiente celda o no lo hace.
 * Esta clase extiende de la clase TickerBehaviour, que define un comportamiento periódico.
 */
public class ComportamientoCoche extends TickerBehaviour {

    /**
     * El período de tiempo entre dos ejecuciones del comportamiento, expresado en milisegundos.
     */
    private static final int period = 1000;

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
        super(a, period);
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


        // Establecer destinatario del mensaje (Agente Scene)
        AID destinatario = coche.getAIDAgenteControlTrafico();
        if(destinatario!=null) {
            ACLMessage mensage = new ACLMessage(ACLMessage.REQUEST);
            mensage.addReceiver(destinatario);
            // Establecer contenido del mensaje
            try {

                mensage.setContent("AvanceCoche");
                mensage.setContentObject(coche.getCeldaActual());

                // Establecer el protocolo FIPA-Request
                mensage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                // Establecer la codificación del mensaje
                mensage.setLanguage("Java");
                mensage.setOntology("Ontologia de Coches");

                // Enviar mensaje
                coche.send(mensage);

                // Esperar respuesta
                ACLMessage respuesta = coche.blockingReceive();
                if (respuesta != null) {
                    if (respuesta.getPerformative() == ACLMessage.AGREE) {
                        CeldaTransitable siguienteCelda = (CeldaTransitable) respuesta.getContentObject();
                        coche.avance(siguienteCelda);
                        System.out.println("El coche "+coche.getAID().getLocalName()+" avanza a "+siguienteCelda.getCoordenadas());
                    } else if (respuesta.getPerformative() == ACLMessage.REFUSE) {
                        //System.out.println("El agente Scene ha rechazado la petición.");
                    } else {
                        //System.out.println("El agente Scene ha respondido con un mensaje no esperado.");
                    }
                } else {
                    block();
                }
            } catch (IOException e) {
               // System.err.println(getClass() + " " + e.getMessage());
            } catch (UnreadableException e) {
              //  System.err.println(getClass() + " " + e.getMessage());
            }
        }
    }
}
