package es.uma.isia.sma.controller;

import es.uma.isia.sma.controller.behaviour.ComportamientoSimulacionEntorno;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.LinkedList;
import java.util.List;

public class AgenteSimuladorEntorno extends Agent {

    private int filas;
    private int columnas;
    private int numeroSegundosSimulacion;
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
        numeroSegundosSimulacion = Integer.parseInt((String) args[2]);
        porcentajeCeldasNoTransitables = Float.parseFloat((String) args[3]);
        porcentajeVehiculos = Float.parseFloat((String) args[4]);
        segundosEsperaNoTransitable = Integer.parseInt((String) args[5]);
        listadoControladoresCoches = new LinkedList<>();
        listadoControladoresSemaforos = new LinkedList<>();

        // Add the generic behaviour
        addBehaviour(new ComportamientoSimulacionEntorno(this));
    }

    public void addCarAgent(String name){
//        ContainerController c = getContainerController();
//        try {
//            //Pasamos la celda inicial
////            PassableCell initCell = (PassableCell) matrix.getCell(matrix.getMatrixIntroPosition());
////            c.createNewAgent(name, "es.uma.isia.sma.agents.CarAgent", new Object[]{ initCell, getAID()});
////            AgentController agentController = c.getAgent(name);
////            agentController.start();
////            listadoControladoresCoches.add(agentController);
//        } catch (StaleProxyException e) {
//            throw new RuntimeException(e);
//        } catch (ControllerException e) {
//            throw new RuntimeException(e);
//        }
    }

//    public void addTrafficLightAgent(CrossCell trafficLightCell){
////        ContainerController c = getContainerController();
////        try {
////            String nickname = "semaforo_"+ trafficLightCell.getMatrixPosition().hashCode();
////            c.createNewAgent(nickname, "es.uma.isia.sma.agents.TrafficLightAgent", new Object[]{ trafficLightCell});
////            AgentController agentController = c.getAgent(nickname);
////            agentController.start();
////            trafficLigthAgents.add(agentController);
////        } catch (StaleProxyException e) {
////            throw new RuntimeException(e);
////        } catch (ControllerException e) {
////            throw new RuntimeException(e);
////        }
//    }

    @Override
    public void doDelete() {
//        for (AgentController agent: carAgents) {
//            try {
//                agent.kill();
//            } catch (StaleProxyException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        for (AgentController agent: trafficLigthAgents) {
//            try {
//                agent.kill();
//            } catch (StaleProxyException e) {
//                throw new RuntimeException(e);
//            }
//        }

        super.doDelete();
    }
}
