package velvetdawn.models.anytype;

public class AnyBool extends AnyFloat {

    public AnyBool(boolean value) {
        super(value ? 1 : 0);
    }

    @Override
    public String toSaveString() {
        return String.format("b#%s", this.value);
    }

    @Override
    public String toString() {
        return this.value == 0 ? "false" : "true";
    }

    public boolean toBool() {
        return this.value == 1;
    }

    @Override
    public AnyFloat validateInstanceIsFloat(String s) throws Exception {
        throw new Exception(s);
    }
    /** Validate the value is a bool type (overriden in the AnyBool) */
    public AnyBool validateInstanceIsBool(String s) {
        return this;
    }
}
