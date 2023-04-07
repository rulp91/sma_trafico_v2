package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteSemaforo;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * ComportamientoSemaforoManejarSuscripciones es un comportamiento cíclico que
 * permite a un agente semáforo manejar las suscripciones de agentes de control de tráfico.
 * El agente semáforo recibirá mensajes de suscripción y almacenará el AID del agente
 * suscriptor para futuras comunicaciones.
 */
public class ComportamientoSemaforoManejarSuscripciones extends CyclicBehaviour {

    AgenteSemaforo agenteSemaforo;

    /**
     * Inicializa la referencia al agente semáforo asociado con este comportamiento.
     *
     * @param a El objeto AgenteSemaforo asociado al comportamiento.
     */
    public ComportamientoSemaforoManejarSuscripciones(AgenteSemaforo a) {
        super(a);
        agenteSemaforo = a;
    }

    /**
     * Método de acción que se ejecuta en cada ciclo del comportamiento.
     * Si se recibe un mensaje de suscripción, actualiza el AID del agente
     * suscriptor en el agente semáforo. Si no se recibe ningún mensaje, el
     * comportamiento se bloquea hasta que haya mensajes nuevos.
     */
    @Override
    public void action() {
        ACLMessage msg = agenteSemaforo.receive();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.SUBSCRIBE) {
                AID suscriptor = msg.getSender();
                agenteSemaforo.setAgenteControlTraficoAID(suscriptor);
            }
        } else {
            block();
        }
    }
}
