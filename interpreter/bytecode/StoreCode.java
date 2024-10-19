package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class StoreCode extends ByteCode {
    private int offset;
    private String id;
    private int value;

    public StoreCode(List<String> code) {
        super(code);
        this.offset = Integer.parseInt(code.get(1));
        if(code.size() > 2) {
            this.id = code.get(2);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        this.value = vm.getRuntimeStack().peek();
        vm.getRuntimeStack().store(this.offset);
    }

    @Override
    public String toString() {
        return String.format("%-25s%s", super.getCode() + " " + this.offset + " " + this.id, this.id + " = " + this.value);
    }
}
