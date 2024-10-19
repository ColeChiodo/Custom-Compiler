package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class WriteCode extends ByteCode{
    
    public WriteCode(List<String> code) {
        super(code);
    }

    @Override
    public void execute(VirtualMachine vm) {
        System.out.println("> " + vm.getRuntimeStack().peek());
    }
}
