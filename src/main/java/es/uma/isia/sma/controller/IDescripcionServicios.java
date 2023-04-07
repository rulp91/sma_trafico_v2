package es.uma.isia.sma.controller;

/**
 * Interfaz que define constantes para las descripciones de servicios utilizadas en el sistema
 * de control de tráfico. Estas descripciones de servicios se utilizan para registrar y buscar
 * agentes en el servicio de directorio (DFService).
 */
public interface IDescripcionServicios {

    /**
     * Descripción del servicio para agentes que representan coches en el sistema.
     */
    String COCHES_SERVICE_DESCRIPTION = "coches";

    /**
     * Descripción del servicio para agentes que se encargan del control del tráfico.
     */
    String CONTROL_TRAFICO_SERVICE_DESCRIPTION = "control-trafico";

    /**
     * Descripción del servicio para agentes que representan semáforos en el sistema.
     */
    String SEMAFOROS_SERVICE_DESCRIPTION = "semaforos";

    /**
     * Descripción del servicio para actualizaciones de estado de semáforos en el sistema.
     */
    String SEMAFOROS_ACTUALIZACIONES_SERVICE_DESCRIPTION = "actualizaciones-semaforo";
}
