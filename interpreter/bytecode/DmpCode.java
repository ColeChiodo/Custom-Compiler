package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class DmpCode extends ByteCode {
    private String flag;

    public DmpCode(List<String> code) {
        super(code);
        this.flag = code.get(1);
    }

    public String getFlag() {
        return this.flag;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.setDMPState(flag);
    }
}
