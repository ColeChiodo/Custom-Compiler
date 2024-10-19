package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class GotoCode extends ByteCode {
    private String label;
    private int address;

    public GotoCode(List<String> code) {
        super(code);
        this.label = code.get(1);
    }

    @Override
    public String toString() {
        return super.getCode() + " " + this.label;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getLabel() {
        return this.label;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.setPC(this.address);
    }
}
