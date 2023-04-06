package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSimulacionEntorno;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase AgenteSimuladorEntorno que extiende de Agent de JADE. Este agente es responsable
 * de la simulación del entorno, la creación de agentes de tráfico y semáforos, y la
 * gestión de la simulación en general.
 */
public class AgenteSimuladorEntorno extends Agent {

    private static final Logger logger = LoggerController.getInstance().getLogger();
    private static final String AGENTE_CONTROL_TRAFICO = "agente-control-trafico";
    private static final String SEMAFORO_PREFIX = "semaforo_";
    private static final String AGENTE_SEMAFORO_CLASS_NAME = "es.uma.isia.sma.controller.AgenteSemaforo";
    private static final String AGENTE_CONTROL_TRAFICO_CLASS_NAME = "es.uma.isia.sma.controller.AgenteControlTrafico";
    public static final String AGENTE_COCHE_CLASS_NAME = "es.uma.isia.sma.controller.AgenteCoche";

    private int filas;
    private int columnas;
    private int timeoutSimulacion;
    private float porcentajeCeldasNoTransitables;
    private float porcentajeVehiculos;
    private int segundosEsperaNoTransitable;

    private List<AgentController> listadoControladoresCoches;
    private List<AgentController> listadoControladoresSemaforos;
    private AgentController controladorTrafico;

    /**
     * Método setup() de JADE que se ejecuta al inicializar el agente.
     * Se encarga de inicializar los atributos del agente y añadir el comportamiento
     * de la simulación del entorno.
     */
    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args == null || args.length == 0)
            doDelete();

        logger.info("Creación de agente:  " + getAID().getLocalName());

        inicializarEntornoSimulacion(args);

        // Añadimos el comportamiento
        addBehaviour(new ComportamientoSimulacionEntorno(this));
    }

    /**
     * Agrega un agente coche al entorno de simulación.
     *
     * @param nombre Nombre del agente coche a agregar.
     */
    public void agregarAgenteCoche(String nombre) {
        ContainerController c = getContainerController();
        try {
            c.createNewAgent(nombre, AGENTE_COCHE_CLASS_NAME, null);
            AgentController agentController = c.getAgent(nombre);
            agentController.start();
            listadoControladoresCoches.add(agentController);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al agregar el agente coche: " + nombre, e);
        }
    }

    /**
     * Inicializa los agentes semáforo en el entorno de simulación.
     */
    public void inicilizarSemaforos() {
        ContainerController c = getContainerController();
        List<Semaforo> semaforos = EntornoUrbanoManager.getInstance().getSemaforos();
        for (Semaforo semaforo : semaforos) {
            try {
                String nickname = SEMAFORO_PREFIX + semaforo.getCoordenadas().x + "_" + semaforo.getCoordenadas().y;
                c.createNewAgent(nickname, AGENTE_SEMAFORO_CLASS_NAME, new Object[]{semaforo});
                AgentController agentController = c.getAgent(nickname);
                agentController.start();
                listadoControladoresSemaforos.add(agentController);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al inicializar los semáforos", e);
            }
        }
    }

    /**
     * Crea un agente de control de tráfico en el entorno de simulación.
     */
    public void crearControlTrafico() {
        EntornoUrbanoManager.getInstance();
        ContainerController c = getContainerController();
        try {
            c.createNewAgent(AGENTE_CONTROL_TRAFICO, AGENTE_CONTROL_TRAFICO_CLASS_NAME, null);
            controladorTrafico = c.getAgent(AGENTE_CONTROL_TRAFICO);
            controladorTrafico.start();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al crear el agente de control de tráfico", e);
        }

    }

    /**
     * Método doDelete() de JADE que se ejecuta cuando se elimina el agente.
     * Se encarga de eliminar todos los agentes de tráfico, semáforos y el agente de control de tráfico.
     */
    @Override
    public void doDelete() {
        try {
            controladorTrafico.kill();
        } catch (StaleProxyException e) {
            logger.log(Level.SEVERE, "Error al matar el controller del control de tráfico", e);
        }
        for (AgentController agent : listadoControladoresSemaforos) {
            try {
                agent.kill();
            } catch (StaleProxyException e) {
                logger.log(Level.SEVERE, "Error al matar el controller del semáforo", e);
            }
        }

        for (AgentController agent : listadoControladoresCoches) {
            try {
                agent.kill();
            } catch (StaleProxyException e) {
                logger.log(Level.SEVERE, "Error al matar el controller del coche", e);
            }
        }

        super.doDelete();
    }


    /**
     * Calcula el número de vehículos a crear en el entorno de simulación (sobre el número total de celdas transitables).
     *
     * @return El número de vehículos a crear.
     */
    public int getNumeroVehiculosACrear() {
        return (int) (EntornoUrbanoManager.getInstance().getNumeroCeldasTransitables() * porcentajeVehiculos);
    }

    /**
     * Retorna el tiempo de la simulación.
     *
     * @return El tiempo de la simulación.
     */
    public int getTimeoutSimulacion() {
        return timeoutSimulacion;
    }

    /**
     * Retorna el porcentaje de celdas no transitables (sobre el número total de celdas).
     *
     * @return número de celdas no transitable
     */
    public int getPorcentajeCeldasNoTransitables() {
        return (int) (filas * columnas * porcentajeCeldasNoTransitables);
    }

    /**
     * Inicializa los atributos del entorno de simulación a partir de los argumentos proporcionados.
     *
     * @param args Argumentos que contienen los valores de los atributos a inicializar.
     */
    private void inicializarEntornoSimulacion(Object[] args) {
        filas = Integer.parseInt((String) args[0]);
        columnas = Integer.parseInt((String) args[1]);
        timeoutSimulacion = Integer.parseInt((String) args[2]);
        porcentajeCeldasNoTransitables = Float.parseFloat((String) args[3]);
        porcentajeVehiculos = Float.parseFloat((String) args[4]);
        segundosEsperaNoTransitable = Integer.parseInt((String) args[5]);
        listadoControladoresCoches = new LinkedList<>();
        listadoControladoresSemaforos = new LinkedList<>();
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }



}
