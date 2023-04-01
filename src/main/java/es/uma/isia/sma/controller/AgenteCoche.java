package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoCoche;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase AgenteCoche que representa un agente coche en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del coche.
 */
public class AgenteCoche extends Agent {

    // Crear un Logger para la clase
    private static final Logger logger = Logger.getLogger(ComportamientoCoche.class.getName());

    static {
        LoggerController.getInstance().setupLogger(logger);
    }

    private CeldaTransitable celdaActual;

    private AID foundAgent;

    @Override
    /**
     * Método setup que se ejecuta al iniciar el agente.
     * Recibe como argumentos el AID del agente escena al que debe enviar mensajes.
     */
    protected void setup() {

        logger.info("My local name is " + getAID().getLocalName());

        //Registro el agente en el DF
        registrarAgente();

        addBehaviour(new ComportamientoCoche(this));
    }

    /**
     * Método que registra el agente en el servicio de directorio (DF) de Jade.
     * Crea una descripción del agente, asigna un servicio de tipo "coches" y lo
     * añade a la descripción del agente. Luego, se registra la descripción del
     * agente en el DF mediante DFService.register(this, dfd).
     */
    private void registrarAgente() {
        // Crear una descripción del agente
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID()); // Asignar el AID (Agent ID) del agente

        // Crear un servicio y asignar un tipo
        ServiceDescription sd = new ServiceDescription();
        sd.setType("coches");
        sd.setName(getLocalName()); // Asignar el nombre local del agente como el nombre del servicio

        // Añadir el servicio a la descripción del agente y registrar en el DF
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            logger.log(Level.WARNING, "Error al registrar el agente en el DF", e);
        }
    }

    /**
     * Método que devuelve la celda transitable actual en la que se encuentra el coche.
     *
     * @return La celda transitable actual en la que se encuentra el coche.
     */
    public CeldaTransitable getCeldaActual() {
        return celdaActual;
    }

    /**
     * Retorna el AID del control de tráfico
     *
     * @return AID del agente que contreola el tráfico
     */
    public AID getAIDAgenteControlTrafico() {

        if (foundAgent != null)
            return foundAgent;

        // Crear una descripción del agente y asignar un tipo de servicio
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("control-trafico");
        dfd.addServices(sd);

        try {
            // Realizar la búsqueda en el DF
            DFAgentDescription[] result = DFService.search(this, dfd);

            // Procesar los resultados y obtener el AID del primer agente encontrado
            if (result.length > 0)
                foundAgent = result[0].getName();
            else
                logger.log(Level.INFO, "No se encontraron agentes con el tipo de servicio especificado.");

        } catch (FIPAException e) {
            logger.log(Level.WARNING, "Error al buscar el AID del agente de control de tráfico", e);
        }

        return foundAgent;
    }

    /**
     * Método que actualiza la celda transitable actual en la que se encuentra el coche.
     *
     * @param celda La nueva celda transitable actual en la que se encuentra el coche.
     */
    public void avance(CeldaTransitable celda) {
        celdaActual = celda;
    }

    @Override
    protected void takeDown() {
        // Eliminar el registro del agente en el DF
        try {
            DFService.deregister(this);
            logger.log(Level.INFO, getLocalName() + ": Deregistrado from the DF.");
        } catch (FIPAException e) {
            logger.log(Level.SEVERE, "Error al eliminar el registro del agente en el DF", e);
        }

        super.takeDown();
    }
}
