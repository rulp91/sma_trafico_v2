package es.uma.isia.sma.controller;

/**
 * Interfaz que define constantes para los tipos de mensajes ACL utilizados en la comunicación
 * entre agentes en el sistema de control de tráfico.
 */
public interface IACLTiposMensaje {

    /**
     * Tipo de mensaje ACL para las actualizaciones de estado del semáforo.
     */
    String ACL_MENSAJE_TIPO_ACTUALIZACION_SEMAFORO = "actualizacion_semaforo";

    /**
     * Tipo de mensaje ACL para el avance de un coche en el sistema.
     */
    String ACL_MENSAJE_TIPO_AVANCE_COCHE = "avance_coche";

    /**
     * Tipo de mensaje ACL para el avance de un coche y la actualización del estado del semáforo.
     */
    String ACL_MENSAJE_TIPO_AVANCE_COCHE_ACTUALIZACION_SEMAFORO = "avance_coche_actualizacion_semaforo";
}
