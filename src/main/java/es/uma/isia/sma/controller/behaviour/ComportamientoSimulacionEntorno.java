package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteSimuladorEntorno;
import es.uma.isia.sma.helper.GeneradorEntornoUrbano;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

public class ComportamientoSimulacionEntorno extends Behaviour {
    private int pasos = 1;
    private int timeout;

    private final AgenteSimuladorEntorno simuladorEntorno;


    public ComportamientoSimulacionEntorno(AgenteSimuladorEntorno a) {
        super(a);
        simuladorEntorno = a;

       // timeout = 7 + a.getNumeroSegundosSimulacion();

    }

    @Override
    public void action() {
        switch (pasos) {
            case 1:
                System.out.println("Paso 1: generar matriz");

                pasos++;
                break;
            case 2:
                System.out.println("Paso 2: genera los bloques");

                pasos++;
                break;
            case 3:
                System.out.println("Paso 3: genera las direcciones");

                pasos++;
                break;
            case 4:
                System.out.println("Paso 4: agente control de tráfico");
                simuladorEntorno.crearControlTrafico();
                pasos++;
                break;
            case 5:
                System.out.println("Paso 5: crea el scheduler de los semáforos");
                simuladorEntorno.inicilizarSemaforos();
                pasos++;
                break;
            case 6:
                System.out.println("Paso 6: crea el scheduler de los coches");
                simuladorEntorno.addBehaviour(new ComportamientoSecuenciaEntradaCoches(simuladorEntorno, simuladorEntorno.getNumeroVehiculosACrear()));
                pasos++;
                break;
            case 7:
                System.out.println("Paso 7: crea el scheduler del tiempo de simulación, dando comienzo a la simulación");
                // Add the TickerBehaviour (period 1 sec)
                simuladorEntorno.addBehaviour(new TickerBehaviour(simuladorEntorno, 1000) {
                    protected void onTick() {
                        System.out.println("Paso " + pasos + " de " + timeout);
                        pasos++;
                    }
                });
                pasos++;
                break;
        }

        // simuladorEntorno.initCarAgentListener();

    }

    public boolean done() {
        return pasos == timeout;
    }

    public int onEnd() {
        myAgent.doDelete();
        return super.onEnd();
    }
}
