package es.isia.sm.helper;

import es.isia.sm.model.Direccion;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorEntornoUrbano {

    private char[][] matriz;
    private int filas;
    private int columnas;
    private Random random = new Random();

    public GeneradorEntornoUrbano(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        matriz = new char[filas][columnas];
        // inicialmente, todas las casillas son transitables
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = '_';
            }
        }
    }

    public char[][] getMatriz() {
        return matriz;
    }

    /**
     * Genera un número específico de casillas no transitables en la matriz.
     *
     * @param numCasillasNoTransitables número de casillas no transitables a generar
     */
    public void generarCasillasNoTransitables(int numCasillasNoTransitables) {

        int casillasNoTransitables = 0;

        // Se genera una casilla no transitable aleatoria hasta que se alcance el número deseado
        while (casillasNoTransitables < numCasillasNoTransitables) {
            int fila = random.nextInt(filas + 1);
            int columna = random.nextInt(columnas + 1);

            // Si la casilla aleatoria está dentro de los límites, no forma un camino cerrado ni es la casilla inicial,
            // se marca como no transitable
            if (estaDentroLimite(fila, columna) && !generaCaminoCerrado(fila, columna) && !esCasillaInicial(fila, columna)) {
                matriz[fila][columna] = '0';
                casillasNoTransitables++;
            }
        }
    }

    /**
     * Asigna las direcciones de todas las casillas de la matriz.
     */
    public void asignarDirecciones() {
        boolean[][] visitado = new boolean[filas][columnas];
        int[] casilla = primeraCasillaSinRecorrer(visitado);
        Direccion direccion = Direccion.ESTE;
        while (!seHaRecorridoTodaLaMatriz(visitado)) {
            busquedaEnProfundidad(visitado, casilla[0], casilla[1], direccion);
            casilla = primeraCasillaSinRecorrer(visitado);
            if (casilla[0] == -1 && casilla[1] == -1)
                break;
        }
    }

    /**
     * Método que repara las colisiones horizontales de los objetos en la matriz.
     * Recorre la matriz buscando las colisiones horizontales y realiza las reparaciones correspondientes.
     * Si la colisión se resuelve moviendo el objeto hacia arriba o hacia abajo, se utiliza el carácter '^' o 'v', respectivamente.
     *
     * @see GeneradorEntornoUrbano buscarColisionesHorizontales()
     * @see GeneradorEntornoUrbano estaDentroLimite(int, int)
     */
    public void reparaColisionesHorizontales() {
        for (Pair<Integer, Point> colision : buscarColisionesHorizontales()) {
            int fila = colision.getKey();
            Point puntoColision = colision.getValue();
            int distanciaBloqueX = 0;
            int distanciaBloqueY = 0;

            if (estaDentroLimite(fila, puntoColision.x - 1) && matriz[fila][puntoColision.x - 1] != '0')
                distanciaBloqueX++;

            if (estaDentroLimite(fila, puntoColision.y + 1) && matriz[fila][puntoColision.y + 1] != '0')
                distanciaBloqueY++;

            int cambioY = 0;
            if (distanciaBloqueX < distanciaBloqueY) {
                cambioY = puntoColision.x;
                if (!estaDentroLimite(fila + 1, cambioY) || (estaDentroLimite(fila, cambioY - 1) && matriz[fila][cambioY - 1] == '0'))
                    matriz[fila][cambioY] = '^';
                else
                    matriz[fila][cambioY] = 'v';
            } else {
                cambioY = puntoColision.y;
                if (!estaDentroLimite(fila - 1, cambioY) || (estaDentroLimite(fila, cambioY + 1) && matriz[fila][cambioY + 1] == '0'))
                    matriz[fila][cambioY] = 'v';
                else
                    matriz[fila][cambioY] = '^';
            }

            //Evaluamos el cambio
            if (matriz[fila][cambioY] == '^' &&
                    (!estaDentroLimite(fila - 1, cambioY) || (matriz[fila - 1][cambioY] == '0' || matriz[fila - 1][cambioY] == 'v')))
                matriz[fila][cambioY] = 'v';

            if (matriz[fila][cambioY] == 'v' &&
                    (!estaDentroLimite(fila + 1, cambioY) || (matriz[fila + 1][cambioY] == '0' || matriz[fila + 1][cambioY] == '^')))
                matriz[fila][cambioY] = '^';

        }
    }

    /**
     * Método que repara las colisiones verticales de los objetos en la matriz.
     * Recorre la matriz buscando las colisiones verticales y realiza las reparaciones correspondientes.
     * Si la colisión se resuelve moviendo el objeto hacia la izquierda o hacia la derecha, se utiliza el carácter '<' o '>', respectivamente.
     * Si la colisión se resuelve con un semáforo, se usa el carácter '8'.
     *
     * @see GeneradorEntornoUrbano buscarColisionesVerticales()
     * @see GeneradorEntornoUrbano estaDentroLimite(int, int)
     */
    public void reparaColisionesVerticales() {
        for (Pair<Integer, Point> colision : buscarColisionesVerticales()) {
            int columna = colision.getKey();
            Point puntoColision = colision.getValue();

            //asignar semaforos
            if (estaDentroLimite(puntoColision.x - 1, columna) && estaDentroLimite(puntoColision.x + 1, columna) &&
                    matriz[puntoColision.x - 1][columna] == matriz[puntoColision.x + 1][columna] && matriz[puntoColision.x - 1][columna] != '0') {
                matriz[puntoColision.x][columna] = '8';
                continue;
            }
            if (estaDentroLimite(puntoColision.y - 1, columna) && estaDentroLimite(puntoColision.y + 1, columna) &&
                    matriz[puntoColision.y - 1][columna] == matriz[puntoColision.x + 1][columna] && matriz[puntoColision.y - 1][columna] != '0') {
                matriz[puntoColision.y][columna] = '8';
                continue;
            }

            int distanciaBloqueX = 0;
            int distanciaBloqueY = 0;

            if (estaDentroLimite(puntoColision.x - 1, columna) && matriz[puntoColision.x - 1][columna] != '0')
                distanciaBloqueX++;

            if (estaDentroLimite(puntoColision.y + 1, columna) && matriz[puntoColision.y + 1][columna] != '0')
                distanciaBloqueY++;

            int cambioY = 0;
            if (distanciaBloqueX < distanciaBloqueY) {
                cambioY = puntoColision.x;
                if (!estaDentroLimite(cambioY, columna + 1) || (estaDentroLimite(cambioY - 1, columna) && matriz[cambioY - 1][columna] == '0'))
                    matriz[cambioY][columna] = '<';
                else
                    matriz[cambioY][columna] = '>';
            } else {
                cambioY = puntoColision.y;
                if (!estaDentroLimite(cambioY, columna - 1) || (estaDentroLimite(cambioY + 1, columna) && matriz[cambioY + 1][columna] == '0'))
                    matriz[cambioY][columna] = '>';
                else
                    matriz[cambioY][columna] = '<';
            }

            //Evaluamos el cambio
            if (matriz[cambioY][columna] == '<' &&
                    (!estaDentroLimite(cambioY, columna - 1) || (matriz[cambioY][columna - 1] == '0' || matriz[cambioY][columna - 1] == '>')))
                matriz[cambioY][columna] = '>';

            if (matriz[cambioY][columna] == '>' &&
                    (!estaDentroLimite(cambioY, columna + 1) || (matriz[cambioY][columna + 1] == '0' || matriz[cambioY][columna + 1] == '<')))
                matriz[cambioY][columna] = '<';
        }
    }

    /**
     * Busca posibles ubicaciones para semáforos en dirección horizontal y los coloca en la matriz.
     * Si encuentra una ubicación válida, reemplaza la dirección original del semáforo por el símbolo '8'.
     */
    public void colocarSemaforosHorizontal() {

        // Buscar posibles semaforos
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length - 1; j++) {
                if (matriz[i][j] == '>' || matriz[i][j] == '<') {
                    if (estaDentroLimite(i, j - 1) && estaDentroLimite(i, j + 1) && matriz[i][j - 1] == matriz[i][j + 1]) {
                        if (estaDentroLimite(i - 1, j) && matriz[i - 1][j] == 'v') {
                            matriz[i][j] = '8';
                            continue;
                        }
                        if (estaDentroLimite(i + 1, j) && matriz[i + 1][j] == '^') {
                            matriz[i][j] = '8';
                        }
                    }

                }
            }
        }
    }

    /**
     * Busca posibles ubicaciones para semáforos en dirección verical y los coloca en la matriz.
     * Si encuentra una ubicación válida, reemplaza la dirección original del semáforo por el símbolo '8'.
     */
    public void colocarSemaforosVertical() {

        // Buscar posibles semaforos
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length - 1; j++) {
                if (matriz[i][j] == '^' || matriz[i][j] == 'v') {
                    if (estaDentroLimite(i - 1, j) && estaDentroLimite(i + 1, j) && matriz[i - 1][j] == matriz[i + 1][j]) {
                        if (estaDentroLimite(i, j - 1) && matriz[i][j - 1] == '>') {
                            matriz[i][j] = '8';
                            continue;
                        }
                        if (estaDentroLimite(i, j + 1) && matriz[i][j + 1] == '<') {
                            matriz[i][j] = '8';
                            continue;
                        }
                    }
                    //cruce completo
                    if (estaDentroLimite(i - 1, j) && matriz[i - 1][j] == matriz[i][j] && estaDentroLimite(i, j - 1) && estaDentroLimite(i, j + 1)
                            && (matriz[i][j - 1] == '<' || matriz[i][j - 1] == '>') && (matriz[i][j + 1] == '<' || matriz[i][j + 1] == '>'))
                        matriz[i][j] = '8';
                }
            }
        }
    }


    /**
     * Verifica si se ha recorrido toda la matriz.
     *
     * @param matrix la matriz a verificar
     * @return true si se ha recorrido toda la matriz, false en caso contrario
     */
    private boolean seHaRecorridoTodaLaMatriz(boolean[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (!matrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Devuelve la posición de la primera casilla sin recorrer en la matriz.
     *
     * @param matrix la matriz a verificar
     * @return la posición de la primera casilla sin recorrer en la matriz
     */
    private int[] primeraCasillaSinRecorrer(boolean[][] matrix) {
        int[] result = {-1, -1}; // devuelve {-1, -1} si no hay ningún elemento falso en la matriz
        for (int i = matrix.length - 1; i >= 0; i--) { // iterar a través de filas de abajo hacia arriba
            for (int j = 0; j < matrix[i].length; j++) { // iterar a través de columnas de izquierda a derecha
                if (!matrix[i][j]) { // si encuentra el primer elemento falso
                    result[0] = i; // guarda la fila de la posición encontrada
                    result[1] = j; // guarda la columna de la posición encontrada
                    return result; // devuelve la posición encontrada
                }
            }
        }

        // si no hay elementos falsos en la matriz, devuelve {-1, -1}
        return result;
    }


    /**
     * Búsqueda en profundidad.
     * Es un algoritmo utilizado en la búsqueda de caminos en grafos o árboles. La idea básica del algoritmo es recorrer
     * el grafo en profundidad antes de retroceder. Esto se hace de manera recursiva,
     * explorando todos los caminos posibles a partir del nodo actual antes de volver atrás
     * y continuar explorando otros caminos
     *
     * @param visitado
     * @param fila
     * @param columna
     * @param direccionAnterior
     */
    private void busquedaEnProfundidad(boolean[][] visitado, int fila, int columna, Direccion direccionAnterior) {

        // Si no está dentro del límite o está ya visitado
        if (!estaDentroLimite(fila, columna) || visitado[fila][columna])
            return;

        visitado[fila][columna] = true;
        if (matriz[fila][columna] == '0')
            return;

        matriz[fila][columna] = direccionAnterior.valor();


        int filaVecino = fila;
        int columnaVecino = columna;
        if (direccionAnterior.equals(Direccion.ESTE))
            columnaVecino += 1;
        else if (direccionAnterior.equals(Direccion.OESTE))
            columnaVecino -= 1;
        else if (direccionAnterior.equals(Direccion.NORTE))
            filaVecino -= 1;
        else if (direccionAnterior.equals(Direccion.SUR))
            filaVecino += 1;


        // Si el siguiente vecino está fuera del límite, gira la casilla actual
        if (!estaDentroLimite(filaVecino, columnaVecino) || matriz[filaVecino][columnaVecino] == '0') {
            // cambia de direccion
            if (direccionAnterior.equals(Direccion.ESTE) || direccionAnterior.equals(Direccion.OESTE)) {
                // Comprueba si hay un bloque al norte o al sur
                int filaNorte = fila - 1;
                int filaSur = fila + 1;
                if (estaDentroLimite(filaNorte, columna) && matriz[filaNorte][columna] != '0') {
                    matriz[fila][columna] = Direccion.NORTE.valor();
                    direccionAnterior = Direccion.NORTE;
                    if (matriz[filaNorte][columna] != '_') {
                        direccionAnterior = Direccion.getDireccionByValor(matriz[filaNorte][columna]);
                    }
                } else if (estaDentroLimite(filaSur, columna) && matriz[filaSur][columna] != '0') {
                    matriz[fila][columna] = Direccion.SUR.valor();
                    direccionAnterior = Direccion.SUR;
                    if (matriz[filaSur][columna] != '_') {
                        direccionAnterior = Direccion.getDireccionByValor(matriz[filaSur][columna]);
                    }
                }

                // si la matriz[fila][columna] tiene direccion asignala a la direccion anterior
                // en caso contrario continua con la que le acabas de asignar
            } else if (direccionAnterior.equals(Direccion.NORTE) || direccionAnterior.equals(Direccion.SUR)) {
                // Comprueba si hay un bloque al este o al oeste
                int columnaOeste = columna - 1;
                int columnaEste = columna + 1;
                if (estaDentroLimite(fila, columnaEste) && matriz[fila][columnaEste] != '0') {
                    matriz[fila][columna] = Direccion.ESTE.valor();
                    direccionAnterior = Direccion.ESTE;
                    if (matriz[fila][columnaEste] != '_') {
                        direccionAnterior = Direccion.getDireccionByValor(matriz[fila][columnaEste]);
                    }
                } else if (estaDentroLimite(fila, columnaOeste) && matriz[fila][columnaOeste] != '0') {
                    matriz[fila][columna] = Direccion.OESTE.valor();
                    direccionAnterior = Direccion.OESTE;
                    if (matriz[fila][columnaOeste] != '_') {
                        direccionAnterior = Direccion.getDireccionByValor(matriz[fila][columnaOeste]);
                    }
                }
            }
        }

        if (direccionAnterior == null)
            return;

        recursionDfs(visitado, fila, columna, direccionAnterior);

        // Se detiene porque no puede continuar, busca el primer vecino libre,
        // si encuentra alguno, cambia la dirección a ese vecino
        Direccion cambioDireccion = evaluaVecinos(fila, columna);
        if (cambioDireccion != null)
            recursionDfs(visitado, fila, columna, cambioDireccion);


    }

    /**
     * Realiza la recursión de la busqueda en profundidad intentando continuar con la dirección anteriormente asignada
     *
     * @param visitado
     * @param fila
     * @param columna
     * @param direccion
     */
    private void recursionDfs(boolean[][] visitado, int fila, int columna, Direccion direccion) {
        try {
            matriz[fila][columna] = direccion.valor();
            if (direccion.equals(Direccion.ESTE))
                busquedaEnProfundidad(visitado, fila, columna + 1, direccion);

            if (direccion.equals(Direccion.OESTE))
                busquedaEnProfundidad(visitado, fila, columna - 1, direccion);

            if (direccion.equals(Direccion.NORTE))
                busquedaEnProfundidad(visitado, fila - 1, columna, direccion);

            if (direccion.equals(Direccion.SUR))
                busquedaEnProfundidad(visitado, fila + 1, columna, direccion);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Evalúa los vecinos de una celda para determinar si hay un camino disponible en esa dirección.
     *
     * @param fila    La fila de la celda que se está evaluando.
     * @param columna La columna de la celda que se está evaluando.
     * @return La dirección del primer vecino encontrado que tenga un camino disponible, o null si no hay vecinos con
     * caminos disponibles.
     */
    private Direccion evaluaVecinos(int fila, int columna) {
        int numFilas = matriz.length;
        int numColumnas = matriz[0].length;

        // Verifica si la celda está en los bordes de la matriz
        boolean marcoSuperior = fila == 0;
        boolean marcoInferior = fila == numFilas - 1;
        boolean marcoIzquierdo = columna == 0;
        boolean marcoDerecho = columna == numColumnas - 1;

        // Imprime los valores de los vecinos de la celda
        if (!marcoSuperior && matriz[fila - 1][columna] == '_')  // Si la celda no está en la primera fila, verifica el vecino de arriba
            return Direccion.NORTE;

        if (!marcoInferior && matriz[fila + 1][columna] == '_')  // Si la celda no está en la última fila, verifica el vecino de abajo
            return Direccion.SUR;

        if (!marcoIzquierdo && matriz[fila][columna - 1] == '_')  // Si la celda no está en la primera columna, verifica el vecino de la izquierda
            return Direccion.OESTE;

        if (!marcoDerecho && matriz[fila][columna + 1] == '_')  // Si la celda no está en la última columna, verifica el vecino de la derecha
            return Direccion.ESTE;

        return null;
    }


    /**
     * Método que busca las colisiones horizontales de los objetos en la matriz.
     * Recorre la matriz en busca de colisiones horizontales entre los objetos y las devuelve en una lista de pares
     * (fila, punto de colisión).
     *
     * @return una lista de pares (fila, punto de colisión) que representan las colisiones horizontales encontradas.
     */
    private List<Pair<Integer, Point>> buscarColisionesHorizontales() {
        java.util.List<Pair<Integer, Point>> colisionesHorizontales = new ArrayList<>();
        // Buscar colisiones horizontales
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length - 1; j++) {
                if (matriz[i][j] == '>' && matriz[i][j + 1] == '<') {
                    colisionesHorizontales.add(new Pair<>(i, new Point(j, j + 1)));
                    System.out.println("Colisión horizontal en la fila " + (i + 1) + ", entre las columnas " + (j + 1) + " y " + (j + 2));
                }
            }
        }

        return colisionesHorizontales;
    }

    /**
     * Método que busca las colisiones verticales de los objetos en la matriz.
     * Recorre la matriz en busca de colisiones verticales entre los objetos y las devuelve en una lista de pares (columna, punto de colisión).
     *
     * @return una lista de pares (columna, punto de colisión) que representan las colisiones verticales encontradas.
     */
    private List<Pair<Integer, Point>> buscarColisionesVerticales() {
        List<Pair<Integer, Point>> colisionesVerticales = new ArrayList<>();
        // Buscar colisiones verticales
        for (int i = 0; i < matriz.length - 1; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == 'v' && matriz[i + 1][j] == '^') {
                    colisionesVerticales.add(new Pair<>(j, new Point(i, i + 1)));
                    System.out.println("Colisión vertical en la columna " + (j + 1) + ", entre las filas " + (i + 1) + " y " + (i + 2));
                }
            }
        }
        return colisionesVerticales;
    }

    /**
     * Indica si pasada una fila y una columna pertenecen a la casilla que se considera de entrada
     *
     * @param fila
     * @param columna
     * @return
     */
    private boolean esCasillaInicial(int fila, int columna) {
        return fila == filas - 1 && columna == 0;
    }


    /**
     * Verifica si la casilla en la posición dada generaría un camino cerrado.
     *
     * @param fila    la fila de la casilla a verificar
     * @param columna la columna de la casilla a verificar
     */
    private boolean generaCaminoCerrado(int fila, int columna) {
        return (celdaLibre(fila - 1, columna) && !celdaLibre(fila - 2, columna)) ||
                (celdaLibre(fila + 1, columna) && !celdaLibre(fila + 2, columna)) ||
                (celdaLibre(fila, columna - 1) && !celdaLibre(fila, columna - 2)) ||
                (celdaLibre(fila, columna + 1) && !celdaLibre(fila, columna + 2));
    }

    /**
     * Una celda la consideramos libre si está dentro de los límites y está marcada como transitable
     *
     * @param fila    la fila está dentro del límite y es transitable
     * @param columna la columna está dentro del límite y es transitable
     * @return
     */
    private boolean celdaLibre(int fila, int columna) {
        return estaDentroLimite(fila, columna) && matriz[fila][columna] == '_';
    }


    /**
     * Indica si una posición está dentro de los límites
     *
     * @param fila    la fila está dentro del límite
     * @param columna la columna está dentro del límite
     * @return
     */
    private boolean estaDentroLimite(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas)
            return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                ret.append(matriz[i][j]).append(" ");
            }
            ret.append("\n");
        }

        return ret.toString();
    }
}
