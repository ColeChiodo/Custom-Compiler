package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class PopCode extends ByteCode {
    private int value;

    public PopCode(List<String> code) {
        super(code);
        this.value = Integer.parseInt(code.get(1));
    }

    @Override
    public String toString() {
        return this.getCode() + " " + this.value;
    }

    @Override
    public void execute(VirtualMachine vm) {
        for (int i = 0; i < this.value; i++)
            vm.getRuntimeStack().pop();
    }
}
