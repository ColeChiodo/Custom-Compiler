package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class LabelCode extends ByteCode {
    private String label;
    private int location;

    public LabelCode(List<String> code) {
        super(code);
        this.label = code.get(1);
    }

    @Override
    public String toString() {
        return super.getCode() + " " + this.label;
    }

    @Override
    public void execute(VirtualMachine vm) {
        this.location = vm.getPC();
    }

    public int getLocation() {
        return location;
    }

    public String getLabel() {
        return label;
    }

}
