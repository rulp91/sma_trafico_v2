package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSimulacionEntorno;
import es.uma.isia.sma.helper.GeneradorEntornoUrbano;
import es.uma.isia.sma.model.celdas.Celda;
import es.uma.isia.sma.model.celdas.Semaforo;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.scene.control.Cell;

import java.util.LinkedList;
import java.util.List;

public class AgenteSimuladorEntorno extends Agent {

    private int filas;
    private int columnas;
    private int timeoutSimulacion;
    private float porcentajeCeldasNoTransitables;
    private float porcentajeVehiculos;
    private int segundosEsperaNoTransitable;

    private List<AgentController> listadoControladoresCoches;
    private List<AgentController> listadoControladoresSemaforos;
    private AgentController controladorTrafico;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args == null || args.length == 0) {
            doDelete();
        }

        filas = Integer.parseInt((String) args[0]);
        columnas = Integer.parseInt((String) args[1]);
        timeoutSimulacion = Integer.parseInt((String) args[2]);
        porcentajeCeldasNoTransitables = Float.parseFloat((String) args[3]);
        porcentajeVehiculos = Float.parseFloat((String) args[4]);
        segundosEsperaNoTransitable = Integer.parseInt((String) args[5]);
        listadoControladoresCoches = new LinkedList<>();
        listadoControladoresSemaforos = new LinkedList<>();

        // Add the generic behaviour
        addBehaviour(new ComportamientoSimulacionEntorno(this));
    }

    public void addCarAgent(String name){
        ContainerController c = getContainerController();
        try {
            // TODO: 30/03/2023 quitar AID y pasar como localname
            //Pasamos la celda inicial
//            PassableCell initCell = (PassableCell) matrix.getCell(matrix.getMatrixIntroPosition());
            c.createNewAgent(name, "es.uma.isia.sma.controller.AgenteCoche", null);
            AgentController agentController = c.getAgent(name);
            agentController.start();
            listadoControladoresCoches.add(agentController);
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
    }
    public void inicilizarSemaforos() {
        ContainerController c = getContainerController();
        List<Semaforo> semaforos = EntornoUrbanoSingleton.getInstance().getSemaforos();
        for (Semaforo semaforo: semaforos) {
            try {
                String nickname = "semaforo_" + semaforo.getCoordenadas().x + "_" + semaforo.getCoordenadas().y;
                c.createNewAgent(nickname, "es.uma.isia.sma.controller.AgenteSemaforo", new Object[]{semaforo});
                AgentController agentController = c.getAgent(nickname);
                agentController.start();
                listadoControladoresSemaforos.add(agentController);
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            } catch (ControllerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void crearControlTrafico() {
        EntornoUrbanoSingleton.getInstance();
        ContainerController c = getContainerController();
        try {
            c.createNewAgent("agente-control-trafico", "es.uma.isia.sma.controller.AgenteControlTrafico", null);
            controladorTrafico = c.getAgent("agente-control-trafico");
            controladorTrafico.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doDelete() {
        try {
            controladorTrafico.kill();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
        for (AgentController agent: listadoControladoresSemaforos) {
            try {
                agent.kill();
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            }
        }

        for (AgentController agent: listadoControladoresCoches) {
            try {
                agent.kill();
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            }
        }

        super.doDelete();
    }


    public int getNumeroVehiculosACrear(){
        //return (int) (filas * columnas * porcentajeVehiculos);
        return 10;
    }

    public int getTimeoutSimulacion() {
        return timeoutSimulacion;
    }
}
