package model;

/**
 *
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {

    private T1 a;
    private T2 b;

    public Pair(T1 a, T2 b) {
        this.a = a;
        this.b = b;
    }

    public void setA(T1 a) {
        this.a = a;
    }

    public void setB(T2 b) {
        this.b = b;
    }

    public T1 getA() {
        return this.a;
    }

    public T2 getB() {
        return this.b;
    }
}
