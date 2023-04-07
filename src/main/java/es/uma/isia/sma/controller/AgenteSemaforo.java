package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSemaforo;
import es.uma.isia.sma.model.celdas.Semaforo;
import es.uma.isia.sma.model.coordenadas.Direccion;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase AgenteSemaforo que representa un agente semáforo en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del semáforo.
 */
public class AgenteSemaforo extends Agent {


    private static final Logger logger = LoggerController.getInstance().getLogger();
    private Semaforo semaforo;
    private AID agenteControlTraficoAID = null;

    @Override
    protected void setup() {

        // Se le pasa como parámetro la celda que ocupa el semaforo
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        logger.info("Creación de agente:  " + getAID().getLocalName());
        semaforo = (Semaforo) args[0];

        //Registro el agente en el DF
        registrarAgente();

        // Añadir comportamiento
        addBehaviour(new ComportamientoSemaforo(this));
    }

    /**
     * Método que devuelve el objeto Semaforo asociado al agente.
     *
     * @return El objeto Semaforo asociado al agente.
     */
    public Semaforo getSemaforo() {
        return semaforo;
    }

    /**
     * Cambia la dirección permitida del semáforo.
     */
    public void cambiarDireccionPermitida() {
        switch (semaforo.getDireccion()) {
            case NORTE:
                semaforo.direccionPermitida(Direccion.ESTE);
                break;
            case ESTE:
                semaforo.direccionPermitida(Direccion.SUR);
                break;
            case SUR:
                semaforo.direccionPermitida(Direccion.OESTE);
                break;
            case OESTE:
                semaforo.direccionPermitida(Direccion.NORTE);
                break;
        }
    }

    /**
     * Retorna el AID del control de tráfico
     *
     * @return AID del agente que contreola el tráfico
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
            if (result.length > 0) {
                agenteControlTraficoAID = result[0].getName();
            } else {
                logger.log(Level.INFO,"No se encontraron agentes con el tipo de servicio especificado.");
            }
        } catch (FIPAException e) {
            logger.log(Level.WARNING, "Error al buscar el AID del agente de control de tráfico", e);
        }

        return agenteControlTraficoAID;
    }

    /**
     * Registra el agente en el DF (Directory Facilitator) con un tipo de servicio "semaforos".
     * El agente se registra con su nombre local como el nombre del servicio.
     */
    private void registrarAgente() {
        // Crear una descripción del agente
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID()); // Asignar el AID (Agent ID) del agente

        // Crear un servicio y asignar un tipo
        ServiceDescription sd = new ServiceDescription();
        sd.setType("semaforos");
        sd.setName(getLocalName()); // Asignar el nombre local del agente como el nombre del servicio

        // Añadir el servicio a la descripción del agente y registrar en el DF
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            logger.log(Level.WARNING, "Error al registrar el agente en el DF", e);
        }
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
