package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteSimuladorEntorno;
import jade.core.behaviours.TickerBehaviour;

/**
 * Comportamiento encargado de crear una secuencia de coches cada segundo, hasta que se han creado todos los coches
 */
public class ComportamientoSecuenciaEntradaCoches extends TickerBehaviour {


    private static final String COCHE_PREFIX = "coche_";
    private int numeroCochesCreado = 0;

    private final int numeroCochesACrear;

    private final AgenteSimuladorEntorno simuladorEntorno;

    /**
     * Crea un comportamiento de secuenciación de la creación de coches dentro de la matriz
     *
     * @param a                  AgenteSimuladorEntorno que ejecuta el comportamiento
     * @param numeroCochesACrear número de coches que se deben crear en la secuencia
     */
    public ComportamientoSecuenciaEntradaCoches(AgenteSimuladorEntorno a, int numeroCochesACrear) {
        super(a, 1000);
        this.simuladorEntorno = a;
        this.numeroCochesACrear = numeroCochesACrear;

    }

    /**
     * Método que se ejecuta cada vez que se cumple el intervalo de tiempo definido.
     * Se encarga de crear un nuevo coche en la secuencia y detener el comportamiento
     * cuando se han creado todos los coches necesarios.
     */
    @Override
    protected void onTick() {
        simuladorEntorno.agregarAgenteCoche(COCHE_PREFIX + numeroCochesCreado);
        numeroCochesCreado++;
        if (numeroCochesCreado == numeroCochesACrear) {
            this.stop();
            super.onEnd();
        }
    }
}
