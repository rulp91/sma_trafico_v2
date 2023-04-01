package es.uma.isia.sma.model.utils;

import java.util.Objects;

public class Par<K, V> {
    public final K clave;
    public final V valor;
    /**
     * Constructor for a Pair.
     *
     * @param clave the first object in the Pair
     * @param valor the second object in the pair
     */
    public Par(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public K getClave() {
        return clave;
    }

    public V getValor() {
        return valor;
    }

    /**
     * Checks the two objects for equality by delegating to their respective
     * {@link Object#equals(Object)} methods.
     *
     * @param o the {@link Par} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered
     *         equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Par) ) {
            return false;
        }
        Par<?, ?> p = (Par<?, ?>) o;
        return Objects.equals(p.clave, clave) && Objects.equals(p.valor, valor);
    }
    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @Override
    public int hashCode() {
        return (clave == null ? 0 : clave.hashCode()) ^ (valor == null ? 0 : valor.hashCode());
    }
    @Override
    public String toString() {
        return "Par{" + String.valueOf(clave) + " " + String.valueOf(valor) + "}";
    }
    /**
     * Convenience method for creating an appropriately typed pair.
     * @param a the first object in the Pair
     * @param b the second object in the pair
     * @return a Pair that is templatized with the types of a and b
     */
    public static <A, B> Par <A, B> create(A a, B b) {
        return new Par<A, B>(a, b);
    }
}
