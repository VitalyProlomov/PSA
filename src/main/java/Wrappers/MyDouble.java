package Wrappers;

public class MyDouble {
    public double val;

    public MyDouble(double val) {
        this.val = val;
    }

    public MyDouble() {
        this.val = 0.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Double) {
            return this.val == (Double)obj;
        }
        if (obj.getClass() == Float.class) {
            return this.val == (Float)obj;
        }
        if (obj.getClass() == MyDouble.class) {
            return val == ((MyDouble)obj).val;
        }
        return false;
    }

}
