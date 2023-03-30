package es.isia.sm;

import es.isia.sm.helper.GeneradorEntornoUrbano;
import es.isia.sm.model.celdas.Celda;


public class Main {
    public static void main(String[] args) {

        GeneradorEntornoUrbano entornoUrbano = new GeneradorEntornoUrbano(10, 20);
        entornoUrbano.generarCasillasNoTransitables(40);
        System.out.println(entornoUrbano);
        entornoUrbano.asignarDirecciones();
        System.out.println(entornoUrbano);

        entornoUrbano.reparaColisionesHorizontales();
        entornoUrbano.reparaColisionesVerticales();
        entornoUrbano.colocarSemaforosHorizontal();
        entornoUrbano.colocarSemaforosVertical();
        System.out.println(entornoUrbano);
        Celda[][] entorno = entornoUrbano.generarEntornoUrbano();
//        //TODO :: volver a revisar colisiones horizontales y aplicar corrección
//        // si aun existen colisiones, coloca semaforos
//        colisionesHorizontales = entornoUrbano.buscarColisionesHorizontales();
//        if(colisionesHorizontales.size() > 0) {
//            entornoUrbano.reparaColisionesHorizontales(colisionesHorizontales);
//            System.out.println(entornoUrbano);
//        }
//
//        colisionesVerticales = entornoUrbano.buscarColisionesVerticales();
//        if(colisionesVerticales.size() > 0) {
//            entornoUrbano.reparaColisionesVerticales(colisionesVerticales);
//            System.out.println(entornoUrbano);
//        }
//        //Semaforos.agregarSemaforos(entornoUrbano.getMatriz());

//        //Revisa la matriz y coloca semaforos
//        BucleDetector b = new BucleDetector(entornoUrbano.getMatriz());
//        if(b.hayBucle()){
//            System.out.println("hay un bucle en la matriz "+ b.getInicio_i() + ","+ b.getInicio_j());
//        }

        System.out.println("Hello world!");
    }
}
