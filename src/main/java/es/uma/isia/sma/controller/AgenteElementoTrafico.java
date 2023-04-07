package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.AgenteControlTrafico;
import es.uma.isia.sma.controller.LoggerController;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase abstracta AgenteElementoTrafico que representa elementos de tráfico, como agentes de coches y semáforos.
 * Esta clase extiende la clase jade.core.Agent y proporciona métodos comunes para el registro y la búsqueda de
 * agentes en el servicio de directorio (DF) de Jade.
 */
public abstract class AgenteElementoTrafico extends Agent {
    // Atributos comunes
    protected static final Logger logger = LoggerController.getInstance().getLogger();
    protected AID agenteControlTraficoAID;

    /**
     * Método que registra el agente en el servicio de directorio (DF) de Jade.
     * El agente se registra con su nombre local como el nombre del servicio.
     *
     * @param serviceDescription Una cadena que representa la descripción del servicio para el agente.
     */
    protected void registrarAgente(String serviceDescription) {
        // Crear una descripción del agente
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID()); // Asignar el AID (Agent ID) del agente

        // Crear un servicio y asignar un tipo
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceDescription);
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
     * Retorna el AID del control de tráfico
     *
     * @return AID del agente que controla el tráfico
     */
    public AID getAIDAgenteControlTrafico() {
        if (agenteControlTraficoAID != null)
            return agenteControlTraficoAID;

        // Crear una descripción del agente y asignar un tipo de servicio
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(AgenteControlTrafico.CONTROL_TRAFICO_SERVICE_DESCRIPTION);
        dfd.addServices(sd);

        try {
            // Realizar la búsqueda en el DF
            DFAgentDescription[] result = DFService.search(this, dfd);

            // Procesar los resultados y obtener el AID del primer agente encontrado
            if (result.length > 0)
                agenteControlTraficoAID = result[0].getName();
            else
                logger.log(Level.INFO, "No se encontraron agentes con el tipo de servicio especificado.");

        } catch (FIPAException e) {
            logger.log(Level.WARNING, "Error al buscar el AID del agente de control de tráfico", e);
        }

        return agenteControlTraficoAID;
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            logger.log(Level.INFO, getLocalName() + ": Deregistrado  from the DF.");
        } catch (FIPAException e) {
            logger.log(Level.SEVERE, "Error al eliminar el registro del agente en el DF", e);
        }

        super.takeDown();
    }
}
