package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class HaltCode extends ByteCode {

    public HaltCode(List<String> code) {
        super(code);
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.stopRunning();
    }
}
