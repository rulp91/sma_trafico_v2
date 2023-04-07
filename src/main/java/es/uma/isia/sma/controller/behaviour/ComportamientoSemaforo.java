package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteSemaforo;
import es.uma.isia.sma.controller.LoggerController;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static es.uma.isia.sma.controller.IACLTiposMensaje.ACL_MENSAJE_TIPO_ACTUALIZACION_SEMAFORO;

/**
 * Clase ComportamientoSemaforo que representa el comportamiento del agente semáforo en el simulador.
 * Extiende la clase WakerBehaviour de Jade y se encarga de cambiar la dirección permitida de paso del semáforo
 * y enviar un mensaje FIPA-ACL al agente escena para informar del cambio.
 */
public class ComportamientoSemaforo extends WakerBehaviour {

    private static final Logger logger = LoggerController.getInstance().getLogger();
    private static final int MIN_PERIOD = 2000;
    private static final int MAX_PERIOD = 5000;

    AgenteSemaforo agenteSemaforo;

    /**
     * Constructor de la clase ComportamientoSemaforo.
     * Recibe como parámetro el objeto AgenteSemaforo asociado al comportamiento.
     * Establece el tiempo de espera entre cambios de dirección del semáforo.
     *
     * @param a El objeto AgenteSemaforo asociado al comportamiento.
     */
    public ComportamientoSemaforo(AgenteSemaforo a) {
        super(a, getBlockTime());
        agenteSemaforo = a;
    }

    @Override
    /**
     * Método que se ejecuta al despertar el comportamiento.
     * Cambia la dirección permitida de paso del semáforo, envía un mensaje FIPA-ACL al agente escena
     * para informar del cambio, y establece el tiempo de espera para el siguiente cambio.
     */
    protected void onWake() {

        agenteSemaforo.cambiarDireccionPermitida();
        enviarMensajeCambioDireccionPermitidaSemaforo();

        // resetea el tiempo
        long timeout = getBlockTime();
        reset(timeout);
    }

    /**
     * Método que envía un mensaje FIPA-ACL al agente escena para informar del cambio en la dirección permitida de paso.
     * El mensaje contiene el objeto Semaforo asociado al agente semáforo.
     */
    public void enviarMensajeCambioDireccionPermitidaSemaforo() {
        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
        mensaje.addReceiver(agenteSemaforo.getAIDAgenteControlTrafico());
        try {
            mensaje.setProtocol(ACL_MENSAJE_TIPO_ACTUALIZACION_SEMAFORO);
            Semaforo semaforo = agenteSemaforo.getSemaforo();
            mensaje.setContentObject(semaforo);
            agenteSemaforo.send(mensaje);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error al enviar el mensaje de cambio de dirección permitida en un semáforo ", e);
        }

    }

    /**
     * Bloquea el Behaviour un tiempo entre 2 y 5 segundos
     *
     * @return tiempo de bloqueo en milisegundos
     */
    private synchronized static long getBlockTime() {
        Random random = new Random();
        long blockTime = random.nextInt(MAX_PERIOD - MIN_PERIOD) + MIN_PERIOD;
        return blockTime;
    }
}
