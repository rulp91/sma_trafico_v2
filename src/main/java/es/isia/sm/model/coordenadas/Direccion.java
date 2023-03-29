package es.isia.sm.model.coordenadas;

import java.util.Random;

/**
 * Enumerado que representa las diferentes direcciones posibles en un mapa.
 * Las direcciones posibles son NORTE, SUR, ESTE, OESTE, SEMAFORO y BLOQUE.
 * SEMAFORO representa un cruce con semáforo y BLOQUE representa un bloque en el mapa.
 */
public enum Direccion {
    NORTE('^'),
    ESTE('>'),
    SUR('v'),
    OESTE('<'),
    SEMAFORO('8'),
    BLOQUE('0'),
    TRANSITABLE('_');

    private final char caracter;

    /**
     * Constructor de la enumeración Direccion.
     * @param caracter El valor que representa la dirección.
     */
    Direccion(char caracter) {
        this.caracter = caracter;
    }

    /**
     * Método que devuelve la dirección correspondiente al valor especificado.
     * @param valor El valor que representa la dirección.
     * @return La dirección correspondiente al valor especificado, o null si no hay ninguna dirección que coincida con el valor.
     */
    public static Direccion getDireccion(char valor) {
        switch (valor) {
            case '^':
                return NORTE;
            case '>':
                return ESTE;
            case 'v':
                return SUR;
            case '<':
                return OESTE;
            case '8':
                return SEMAFORO;
            case '0':
                return BLOQUE;
            case '_':
                return TRANSITABLE;
            default:
                throw new IllegalArgumentException("El valor " + valor + " no corresponde a ninguna dirección conocida.");
        }
    }

    public char valor() {
        return caracter;
    }

    /**
     * Genera una dirección de forma aleatoria
     * @return
     */
    public static Direccion generarDireccionAleatoria() {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(4); // Genera un número aleatorio entre 0 y 3

        switch (numeroAleatorio) {
            case 0:
                return Direccion.NORTE;
            case 1:
                return Direccion.ESTE;
            case 2:
                return Direccion.SUR;
            case 3:
                return Direccion.OESTE;
            default:
                throw new IllegalStateException("Número aleatorio fuera de rango");
        }
    }
}
