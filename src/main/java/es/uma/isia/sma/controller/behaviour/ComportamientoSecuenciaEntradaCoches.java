package es.uma.isia.sma.controller.behaviour;

import es.uma.isia.sma.controller.AgenteSimuladorEntorno;
import jade.core.behaviours.TickerBehaviour;

public class ComportamientoSecuenciaEntradaCoches extends TickerBehaviour {

    private static final int period = 1000;
    private int numeroCochesCreado = 0;

    private final int numeroCochesACrear;

    private final AgenteSimuladorEntorno simuladorEntorno;
    public ComportamientoSecuenciaEntradaCoches(AgenteSimuladorEntorno a, int numeroCochesACrear) {
        super(a, period);
        this.simuladorEntorno = a;
        this.numeroCochesACrear = numeroCochesACrear;

    }

    @Override
    protected void onTick() {
        simuladorEntorno.addCarAgent("coche_" + numeroCochesCreado);
        numeroCochesCreado++;
        if (numeroCochesCreado == numeroCochesACrear) {
            this.stop();
            super.onEnd();
        }

    }
}
