package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class ReadCode extends ByteCode{

    public ReadCode(List<String> code) {
        super(code);
    }

    @Override
    public void execute(VirtualMachine vm) {
        System.out.print("> ");
        int input = Integer.parseInt(System.console().readLine());
        vm.getRuntimeStack().push(input);
    }
}
