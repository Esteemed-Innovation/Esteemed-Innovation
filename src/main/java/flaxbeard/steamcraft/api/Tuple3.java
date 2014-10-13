package flaxbeard.steamcraft.api;

public class Tuple3 {
    public Object first;
    public Object second;
    public Object third;

    public Tuple3(Object a, Object b, Object c) {
        this.first = a;
        this.second = b;
        this.third = c;
    }

    public Object get(int x) {
        switch (x) {
            case 0:
                return this.first;
            case 1:
                return this.second;
            case 2:
                return this.third;
            default:
                return null;
        }
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