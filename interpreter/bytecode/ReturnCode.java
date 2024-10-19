package interpreter.bytecode;

import java.util.List;

import interpreter.VirtualMachine;

public class ReturnCode extends ByteCode{
    private String label;
    private int value;

    public ReturnCode(List<String> code) {
        super(code);
        if(code.size() > 1) {
            this.label = code.get(1);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        this.value = vm.getRuntimeStack().peek();
        vm.returnTo();
        vm.getRuntimeStack().popFrame();
    }

    @Override
    public String toString() {
        String result;

        if(this.label == null) {
            return String.format("%-25s%s", "RETURN" , "end: " + this.value);
        } else {
            if(this.label.contains("<<")) {
                result = this.label.substring(0, this.label.indexOf("<<"));
            }
            else {
                result = this.label;
            }
            return String.format("%-25s%s", "RETURN" + " " + this.label, "end " + result + ": " + this.value);
        }
    }
}
