package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class ArgsCode extends ByteCode {
    private int offset;

    public ArgsCode(List<String> code) {
        super(code);
        this.offset = Integer.parseInt(code.get(1));
    }

    @Override
    public String toString() {
        return super.getCode() + " " + this.offset;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.getRuntimeStack().newFrameAt(this.offset);
        
    }
}
