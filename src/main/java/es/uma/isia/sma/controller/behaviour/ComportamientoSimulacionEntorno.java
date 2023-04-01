package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteSimuladorEntorno;
import es.uma.isia.sma.controller.LoggerController;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

import java.util.logging.Logger;

/**
 * Comportamiento encargado de simular el entorno de la simulación de tráfico.
 * Se compone de una serie de pasos que se van ejecutando secuencialmente que crean los distintos elementos del entorno
 * (matriz, bloques, direcciones, agentes de control de tráfico, semáforos, coches).
 * Además, se añade un comportamiento ciclico que se ejecuta cada segundo hasta completar el tiempo de simulación.
 */
public class ComportamientoSimulacionEntorno extends Behaviour {
    private static final Logger logger = LoggerController.getInstance().getLogger();

    private int pasos = 1;
    private int timeout;

    private final AgenteSimuladorEntorno simuladorEntorno;

    /**
     * Crea un comportamiento para la simulación del entorno a partir del agente simulador recibido como parámetro.
     * El comportamiento se ejecuta durante un tiempo máximo de simulación calculado a partir del tiempo máximo definido
     * en el agente simulador.
     *
     * @param a AgenteSimuladorEntorno al que pertenece el comportamiento.
     */
    public ComportamientoSimulacionEntorno(AgenteSimuladorEntorno a) {
        super(a);
        simuladorEntorno = a;
        timeout = 7 + simuladorEntorno.getTimeoutSimulacion();
    }

    /**
     * Método que define la secuencia de acciones a realizar en cada paso del comportamiento.
     */
    @Override
    public void action() {
        switch (pasos) {
            case 1:
                logger.info("Paso 1: generar matriz");

                pasos++;
                break;
            case 2:
                logger.info("Paso 2: genera los bloques");

                pasos++;
                break;
            case 3:
                logger.info("Paso 3: genera las direcciones");

                pasos++;
                break;
            case 4:
                logger.info("Paso 4: agente control de tráfico");
                simuladorEntorno.crearControlTrafico();
                pasos++;
                break;
            case 5:
                logger.info("Paso 5: crea el scheduler de los semáforos");
                simuladorEntorno.inicilizarSemaforos();
                pasos++;
                break;
            case 6:
                logger.info("Paso 6: crea el scheduler de los coches");
                simuladorEntorno.addBehaviour(new ComportamientoSecuenciaEntradaCoches(simuladorEntorno, simuladorEntorno.getNumeroVehiculosACrear()));
                pasos++;
                break;
            case 7:
                System.out.println("Paso 7: crea el scheduler del tiempo de simulación, dando comienzo a la simulación");
                // Añadimos un comportamiento que se ejecuta cada segundo hasta completar el tiempo de simulación
                simuladorEntorno.addBehaviour(new TickerBehaviour(simuladorEntorno, 1000) {
                    protected void onTick() {
                        logger.info("Paso " + pasos + " de " + timeout);
                        pasos++;
                    }
                });
                pasos++;
                break;
        }

    }

    public boolean done() {
        return pasos == timeout;
    }

    /**
     * Realiza la limpieza de los recursos utilizados por el agente `AgenteSimuladorEntorno`
     * una vez que se ha completado la simulación del entorno.
     *
     * @return el valor devuelto por el método `onEnd()` de la superclase `Behaviour`.
     */
    public int onEnd() {
        simuladorEntorno.doDelete();
        return super.onEnd();
    }
}
