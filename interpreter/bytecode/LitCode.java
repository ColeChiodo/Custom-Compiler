package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class LitCode extends ByteCode {
    private int value;
    private String id;

    public LitCode(List<String> code) {
        super(code);
        this.value = Integer.parseInt(code.get(1));
        if(code.size() > 2) {
            this.id = code.get(2);
        }
    }

    @Override
    public String toString() {
        if(this.id == null) {
            return String.format("%-25s%s", super.getCode() + " " + this.value, "int " + this.value);
        } else {
            return String.format("%-25s%s", super.getCode() + " " + this.value + " " + this.id, "int " + this.id + " = " + this.value);
        }
        
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.getRuntimeStack().push(this.value);
    }
    
}