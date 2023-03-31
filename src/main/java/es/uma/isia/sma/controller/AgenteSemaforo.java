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

/**
 * Clase AgenteSemaforo que representa un agente semáforo en el simulador.
 * Extiende la clase Agent de Jade y se encarga de gestionar el comportamiento
 * del semáforo.
 */
public class AgenteSemaforo extends Agent {
    private Semaforo semaforo;


    @Override
    protected void setup() {
        // Se le pasa como parámetro la celda que ocupa el semaforo
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        System.out.println("My local name is " + getAID().getLocalName());
        semaforo = (Semaforo) args[0];


        //Registro el agente en el DF
        registrarAgente();

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
     * Método que devuelve el AID del agente escena al que debe enviar mensajes.
     *
     * @return El AID del agente escena al que debe enviar mensajes.
     */
    public void cambiaDireccionPermitida() {
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

        AID foundAgent = null;

        // Crear una descripción del agente y asignar un tipo de servicio
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("control-trafico");
        dfd.addServices(sd);

        try {
            // Realizar la búsqueda en el DF
            DFAgentDescription[] result = DFService.search(this, dfd);

            // Procesar los resultados y obtener el AID del primer agente encontrado
            if (result.length > 0) {
                foundAgent = result[0].getName();
            } else {
                System.out.println("No se encontraron agentes con el tipo de servicio especificado.");
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return foundAgent;
    }

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
            System.err.println(e);
        }
    }

    @Override
    protected void takeDown() {
        // Eliminar el registro del agente en el DF
        // Eliminar el registro del agente en el DF
        try {
            DFService.deregister(this);
            System.out.println(getLocalName() + ": Deregistrado  from the DF.");
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        super.takeDown();
    }
}
