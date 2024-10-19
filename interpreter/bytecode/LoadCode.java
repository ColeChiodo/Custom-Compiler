package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class LoadCode extends ByteCode {
    private int offset;
    private String id;

    public LoadCode(List<String> code) {
        super(code);
        this.offset = Integer.parseInt(code.get(1));
        this.id = code.get(2);
    }

    @Override
    public String toString() {
        return String.format("%-25s%s", super.getCode() + " " + this.offset + " " + this.id, "<load " + this.id + ">");
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.getRuntimeStack().load(this.offset);
    }
}
