package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class FalsebranchCode extends ByteCode {
    private String name;
    private int address;

    public FalsebranchCode(List<String> code) {
        super(code);
        this.name = code.get(1);
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getLabel() {
        return this.name;
    }

    @Override
    public String toString() {
        return super.getCode() + " " + this.name;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int top = vm.getRuntimeStack().pop();
        if (top == 0) {
            vm.setPC(this.address);
        }
    }
}
