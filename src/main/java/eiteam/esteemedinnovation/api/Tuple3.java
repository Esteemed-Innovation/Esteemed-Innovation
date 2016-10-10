package eiteam.esteemedinnovation.api;

public class Tuple3<F, S, T> {
    private F first;
    private S second;
    private T third;

    public Tuple3(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    @Override
    public String toString() {
        return getFirst() + " " + getSecond() + " " + getThird();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple3) {
            Tuple3 tuple = (Tuple3) obj;
            return getFirst().equals(tuple.getFirst()) && getSecond().equals(tuple.getSecond()) && getThird().equals(tuple.getThird());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getFirst().hashCode() + getSecond().hashCode() + getThird().hashCode());
    }
}