package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoCentralitaRecepcionMensajesCoches;
import es.uma.isia.sma.controller.behaviour.ComportamientoCoche;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.CeldaTransitable;
import es.uma.isia.sma.model.coordenadas.Coordenada;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgenteControlTrafico extends Agent {
    private Celda[][] entornoUrbano;
    private boolean[][] posicionesOcupadas;

    protected void setup() {

        System.out.println("My local name is " + getAID().getLocalName());

        //Recuperamos la matriz de tráfico
        entornoUrbano = EntornoUrbanoSingleton.getInstance().getEntornoUrbano();
        incicilizarPosicionesOcupdas();

        //Registro el agente en el DF
        registrarAgente();
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);
        parallelBehaviour.addSubBehaviour(new ComportamientoCentralitaRecepcionMensajesCoches(this));
        parallelBehaviour.addSubBehaviour(new ReceiveSemaforoUpdateBehaviour());
        addBehaviour(parallelBehaviour);

    }

    private void incicilizarPosicionesOcupdas() {
        int filas = entornoUrbano.length;
        int columnas = entornoUrbano[0].length;

        posicionesOcupadas = new boolean[filas][columnas];

        // Inicializa la matriz posicionesOcupadas con 'false' (ninguna posición ocupada)
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                posicionesOcupadas[i][j] = false;
            }
        }
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

    public synchronized boolean estaOcupada(Coordenada coordenadas) {
        return posicionesOcupadas[coordenadas.x][coordenadas.y];
    }

    public synchronized void marcarPosicion(Coordenada coordenadas, boolean ocupada) {
         posicionesOcupadas[coordenadas.x][coordenadas.y] = ocupada;
    }

    private class ReceiveSemaforoUpdateBehaviour extends SimpleBehaviour {
        private boolean done = false;

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchContent("CambioDireccionPermitidaSemaforo")
            );
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // Procesar el mensaje de AgenteSemaforo
                // ...

                // Establecer done a true si deseas que este comportamiento se ejecute solo una vez
                // Establecer done a false si deseas que este comportamiento se ejecute continuamente
                done = false;
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return done;
        }
    }

}
