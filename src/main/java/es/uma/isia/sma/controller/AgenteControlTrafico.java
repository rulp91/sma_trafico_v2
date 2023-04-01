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
import java.util.logging.Logger;

/**
 * La clase AgenteControlTrafico es un agente que se encarga de administrar y controlar el tráfico
 * en un entorno urbano. Este agente mantiene un registro de las posiciones ocupadas en el entorno
 * y se comunica con otros agentes como coches y semáforos para coordinar sus acciones.
 */
public class AgenteControlTrafico extends Agent {

    private static final Logger logger = LoggerController.getInstance().getLogger();
    private Celda[][] entornoUrbano;
    private boolean[][] posicionesOcupadas;
    private List<Semaforo> semaforos;

    /**
     * Método que se ejecuta cuando se inicia el agente. Registra el agente en el DF
     * e inicializa el entorno urbano y los comportamientos del agente.
     */
    protected void setup() {
        logger.info("Creación de agente:  " + getAID().getLocalName());
        inicializarEntornoUrbano();
        registrarAgente();
        iniciarComportamientos();
    }

    /**
     * Inicializa el entorno urbano y la lista de semáforos a partir de la instancia de EntornoUrbanoManager.
     */
    private void inicializarEntornoUrbano() {
        EntornoUrbanoManager instancia = EntornoUrbanoManager.getInstance();
        entornoUrbano = instancia.getEntornoUrbano();
        semaforos = instancia.getSemaforos();
        inicializarPosicionesOcupadas();
    }

    /**
     * Inicializa la matriz de posiciones ocupadas con todas las posiciones desocupadas.
     */
    private void inicializarPosicionesOcupadas() {
        int filas = entornoUrbano.length;
        int columnas = entornoUrbano[0].length;

        posicionesOcupadas = new boolean[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                posicionesOcupadas[i][j] = false;
            }
        }
    }

    /**
     * Registra el agente en el DF con un servicio de tipo "control-trafico".
     */
    private void registrarAgente() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("control-trafico");
        sd.setName(getLocalName());

        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println(e);
        }
    }

    /**
     * Inicia los comportamientos del agente, agregándolos como subcomportamientos en un ParallelBehaviour.
     */
    private void iniciarComportamientos() {
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);
        parallelBehaviour.addSubBehaviour(new ComportamientoCentralitaRecepcionMensajesCoches(this));
        parallelBehaviour.addSubBehaviour(new ComportamientoCentralitaRecepcionMensajesSemaforos(this));
        addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            logger.info(getLocalName() + ": Deregistrado from the DF.");
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        super.takeDown();
    }

    /**
     * Comprueba si una posición en el entorno urbano está ocupada.
     *
     * @param coordenadas Las coordenadas de la posición a verificar.
     * @return true si la posición está ocupada, false en caso contrario.
     */
    public synchronized boolean esPosicionOcupada(Coordenada coordenadas) {
        return posicionesOcupadas[coordenadas.x][coordenadas.y];
    }

    /**
     * Marca una posición en el entorno urbano como ocupada o desocupada.
     *
     * @param coordenadas Las coordenadas de la posición a marcar.
     * @param ocupada     true si la posición debe marcarse como ocupada, false si debe marcarse como desocupada.
     */
    public synchronized void marcarPosicion(Coordenada coordenadas, boolean ocupada) {
        posicionesOcupadas[coordenadas.x][coordenadas.y] = ocupada;
    }

    /**
     * Actualiza la dirección permitida en un semáforo y envía un mensaje a los coches afectados si es necesario.
     *
     * @param semaforo El semáforo cuya dirección permitida ha cambiado.
     */
    public synchronized void actualizarDireccionPermitidaSemaforo(Semaforo semaforo) {
        if (semaforos.contains(semaforo) && esPosicionOcupada(semaforo.getCoordenadas())) {
            // TODO: 01/04/2023 "Informar cambio de direccion para coche"
            logger.info("Informar cambio de direccion para coche en el semaforo " + semaforo.getCoordenadas() + " direccion " + semaforo.getDireccion());
        }
    }
}
