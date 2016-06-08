package flaxbeard.steamcraft.api;

public class Tuple3<F, S, T> {
    public F first;
    public S second;
    public T third;

    public Tuple3(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public String toString() {
        return (this.first.toString() + " " + this.second.toString() + " " + this.third.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple3) {
            Tuple3 tuple = (Tuple3) obj;
            if (this.first.equals(tuple.first) && this.second.equals(tuple.second) && this.third.equals(tuple.third)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (first.hashCode() + second.hashCode() + third.hashCode());
    }
}