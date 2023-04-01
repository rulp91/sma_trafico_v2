package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoCentralitaRecepcionMensajesCoches;
import es.uma.isia.sma.controller.behaviour.ComportamientoCentralitaRecepcionMensajesSemaforos;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.Semaforo;
import es.uma.isia.sma.model.coordenadas.Coordenada;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.List;

public class AgenteControlTrafico extends Agent {
    private Celda[][] entornoUrbano;
    private boolean[][] posicionesOcupadas;

    private List<Semaforo> semaforos;

    protected void setup() {

        System.out.println("My local name is " + getAID().getLocalName());

        //Recuperamos la matriz de tráfico
        EntornoUrbanoManager instancia = EntornoUrbanoManager.getInstance();
        entornoUrbano = instancia.getEntornoUrbano();
        semaforos = instancia.getSemaforos();
        incicilizarPosicionesOcupadas();

        //Registro el agente en el DF
        registrarAgente();
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);
        parallelBehaviour.addSubBehaviour(new ComportamientoCentralitaRecepcionMensajesCoches(this));
        parallelBehaviour.addSubBehaviour(new ComportamientoCentralitaRecepcionMensajesSemaforos(this));
        addBehaviour(parallelBehaviour);

    }

    private void incicilizarPosicionesOcupadas() {
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

    public synchronized void cambioDireccionPermitidaSemaforo(Semaforo semaforo) {
        if(semaforos.contains(semaforo) && estaOcupada(semaforo.getCoordenadas())){
            System.out.println("Informar cambio de direccion para coche");
        }
    }


}
