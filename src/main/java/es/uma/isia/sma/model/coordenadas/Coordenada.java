package es.uma.isia.sma.model.coordenadas;

import java.io.Serializable;
import java.util.Objects;

public class Coordenada implements Serializable {


    private static final long serialVersionUID = -9023559829650531111L;

    /**
     * The X coordinate of this <code>Point</code>.
     * If no X coordinate is set it will default to 0.
     *
     * @see #move(int, int)
     * @since 1.0
     */
    public int x;

    /**
     * The Y coordinate of this <code>Point</code>.
     * If no Y coordinate is set it will default to 0.
     *
     * @see #move(int, int)
     * @since 1.0
     */
    public int y;

    /**
     * Constructs and initializes a point at the origin
     * (0,&nbsp;0) of the coordinate space.
     * @since       1.1
     */
    public Coordenada() {
        this(0, 0);
    }

    /**
     * Constructs and initializes a point with the same location as
     * the specified <code>Point</code> object.
     * @param       p a point
     * @since       1.1
     */
    public Coordenada(Coordenada p) {
        this(p.x, p.y);
    }

    /**
     * Constructs and initializes a point at the specified
     * {@code (x,y)} location in the coordinate space.
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     * @since 1.0
     */
    public Coordenada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public double getX() {
        return x;
    }

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the location of the point to the specified location.
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * @param       p  a point, the new location for this point
     * @see         java.awt.Component#setLocation(java.awt.Point)
     * @see         java.awt.Point#getLocation
     * @since       1.1
     */
    public void setLocation(Coordenada p) {
        setLocation(p.x, p.y);
    }

    /**
     * Changes the point to have the specified location.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * Its behavior is identical with <code>move(int,&nbsp;int)</code>.
     * @param       x the X coordinate of the new location
     * @param       y the Y coordinate of the new location
     * @see         java.awt.Component#setLocation(int, int)
     * @see         java.awt.Point#getLocation
     * @see         java.awt.Point#move(int, int)
     * @since       1.1
     */
    public void setLocation(int x, int y) {
        move(x, y);
    }

    /**
     * Sets the location of this point to the specified double coordinates.
     * The double values will be rounded to integer values.
     * Any number smaller than <code>Integer.MIN_VALUE</code>
     * will be reset to <code>MIN_VALUE</code>, and any number
     * larger than <code>Integer.MAX_VALUE</code> will be
     * reset to <code>MAX_VALUE</code>.
     *
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     */
    public void setLocation(double x, double y) {
        this.x = (int) Math.floor(x+0.5);
        this.y = (int) Math.floor(y+0.5);
    }

    /**
     * Moves this point to the specified location in the
     * {@code (x,y)} coordinate plane. This method
     * is identical with <code>setLocation(int,&nbsp;int)</code>.
     * @param       x the X coordinate of the new location
     * @param       y the Y coordinate of the new location
     * @see         java.awt.Component#setLocation(int, int)
     */
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates this point, at location {@code (x,y)},
     * by {@code dx} along the {@code x} axis and {@code dy}
     * along the {@code y} axis so that it now represents the point
     * {@code (x+dx,y+dy)}.
     *
     * @param       dx   the distance to move this point
     *                            along the X axis
     * @param       dy    the distance to move this point
     *                            along the Y axis
     */
    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordenada) {
            Coordenada pt = (Coordenada)obj;
            return (x == pt.x) && (y == pt.y);
        }
        return super.equals(obj);
    }

    /**
     * Returns a string representation of this point and its location
     * in the {@code (x,y)} coordinate space. This method is
     * intended to be used only for debugging purposes, and the content
     * and format of the returned string may vary between implementations.
     * The returned string may be empty but may not be <code>null</code>.
     *
     * @return  a string representation of this point
     */
    public String toString() {
        return "(x=" + x + ",y=" + y + ")";
    }
}
