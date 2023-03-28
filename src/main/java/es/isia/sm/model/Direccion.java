package es.isia.sm.model;


public class Direccion {

    public static final Direccion NORTE = new Direccion(0, 1);
    public static final Direccion SUR = new Direccion(0, -1);
    public static final Direccion ESTE = new Direccion(1, 0);
    public static final Direccion OESTE = new Direccion(-1, 0);

    private final int dx;
    private final int dy;

    private Direccion(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public Direccion inversa() {
        return new Direccion(-dx, -dy);
    }

    public Direccion izquierda() {
        return new Direccion(dy, -dx);
    }

    public Direccion derecha() {
        return new Direccion(-dy, dx);
    }

    public char valor() {
        if (dx == 0 && dy == 1) {
            return '^';
        } else if (dx == 1 && dy == 0) {
            return '>';
        } else if (dx == 0 && dy == -1) {
            return 'v';
        } else if (dx == -1 && dy == 0) {
            return '<';
        } else {
            return '_';
        }
    }

    public static Direccion getDireccionByValor(char valor){
        switch (valor){
            case '^':
                return NORTE;
            case '>':
                return ESTE;
            case 'v':
                return SUR;
            case '<':
                return OESTE;

        }

        return null;
    }
}
