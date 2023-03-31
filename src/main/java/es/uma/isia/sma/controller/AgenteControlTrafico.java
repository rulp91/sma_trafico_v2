package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoCoche;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class AgenteControlTrafico extends Agent{

    protected void setup() {

        System.out.println("My local name is " + getAID().getLocalName());

        //Registro el agente en el DF
        registrarAgente();
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

            }

        });

    }

    private void registrarAgente() {
        // Crear una descripción del agente
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID()); // Asignar el AID (Agent ID) del agente

        // Crear un servicio y asignar un tipo
        ServiceDescription sd = new ServiceDescription();
        sd.setType("control-trafico");
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
        try {
            DFService.deregister(this);
            System.out.println(getLocalName() + ": Deregistrado  from the DF.");
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        super.takeDown();
    }
}
